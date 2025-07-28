package com.example.JobPortal.Controller;

import com.example.JobPortal.Model.*;
import com.example.JobPortal.Model.DTO.AppliedJobResponseDTO;
import com.example.JobPortal.Model.DTO.JobResponseDTO;
import com.example.JobPortal.Model.DTO.UserResponseDTO;
import com.example.JobPortal.Repository.JobApplicationRepo;
import com.example.JobPortal.Repository.JobRepo;
import com.example.JobPortal.Repository.UserRepo;
import com.example.JobPortal.Security.JwtUtil;
import com.example.JobPortal.Service.MinioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private JobApplicationRepo jobApplicationRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MinioService minioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateLoggedInUser(
            @RequestParam String mobileNo,
            @RequestParam String location,
            @RequestParam(required = false) MultipartFile resume,
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String jwtToken = authHeader.substring(7);
        String email = jwtUtil.getUserEmailFromToken(jwtToken);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = optionalUser.get();

        user.setMobileNo(mobileNo);
        user.setLocation(location);

        if (resume != null && !resume.isEmpty()) {
            String resumeUrl = minioService.uploadFile(resume);
            user.setResumeurl(resumeUrl);
        }

        userRepository.save(user);

        return ResponseEntity.ok().body(Map.of("message", "Details updated successfully"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.getUserEmailFromToken(token);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(400).body(Map.of("message", "Old password is incorrect"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @GetMapping("/all-jobs")
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        List<Job> jobs = jobRepo.findAll();

        List<JobResponseDTO> response = jobs.stream().map(job ->
                new JobResponseDTO(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getExperience(),
                        job.getLocation(),
                        job.getJobType(),
                        job.getDepartment(),
                        job.getSalary(),
                        job.getPostedDate(),
                        job.getCompany().getId(),
                        job.getCompany().getName() // Flattened company name
                )
        ).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/jobs-search")
    public ResponseEntity<List<JobResponseDTO>> searchJobs(@RequestParam String keyword) {
        List<Job> jobs = jobRepo.searchJobs(keyword);

        List<JobResponseDTO> response = jobs.stream().map(job ->
                new JobResponseDTO(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getExperience(),
                        job.getLocation(),
                        job.getJobType(),
                        job.getDepartment(),
                        job.getSalary(),
                        job.getPostedDate(),
                        job.getCompany().getId(),
                        job.getCompany().getName()
                )
        ).toList();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/jobs/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        Optional<Job> optionalJob = jobRepo.findById(id);

        if (optionalJob.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Job not found"));
        }

        Job job = optionalJob.get();

        JobResponseDTO response = new JobResponseDTO(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getExperience(),
                job.getLocation(),
                job.getJobType(),
                job.getDepartment(),
                job.getSalary(),
                job.getPostedDate(),
                job.getCompany().getId(),
                job.getCompany().getName()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/view-details")
    public ResponseEntity<?> getLoggedInUserDetails(Authentication authentication) {
        String email = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        User user = userOpt.get();

        UserResponseDTO response = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMobileNo(),
                user.getLocation(),
                user.getRole(),
                user.getResumeurl()
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/applied-jobs")
    public ResponseEntity<?> getAppliedJobs(Authentication authentication) {
        String email = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        User user = userOpt.get();

        List<JobApplication> applications = jobApplicationRepo.findByUser(user);

        List<AppliedJobResponseDTO> response = applications.stream().map(app ->
                new AppliedJobResponseDTO(
                        app.getJob().getId(),
                        app.getJob().getTitle(),
                        app.getJob().getDescription(),
                        app.getJob().getExperience(),
                        app.getJob().getLocation(),
                        app.getJob().getJobType(),
                        app.getJob().getDepartment(),
                        app.getJob().getSalary(),
                        app.getJob().getCompany().getName(),
                        app.getStatus(),
                        app.getAppliedDate()
                )
        ).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<?> applyForJob(@PathVariable Long jobId, Authentication authentication) {
        String email = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        Optional<Job> jobOpt = jobRepo.findById(jobId);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Job not found"));
        }

        Optional<JobApplication> existing = jobApplicationRepo.findByUserAndJob(userOpt, jobOpt);
        if (existing.isPresent()) {
            return ResponseEntity.status(409).body(Map.of("message", "Already applied"));
        }

        User user = userOpt.get();
        Job job = jobOpt.get();

        JobApplication jobApplication = new JobApplication(
                user,
                job,
                ApplicationStatus.APPLIED,
                LocalDate.now()
        );

        jobApplicationRepo.save(jobApplication);

        return ResponseEntity.ok(Map.of("message", "Job application submitted successfully"));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalJobsCount() {
        long count = jobRepo.count();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/apply-count")
    public ResponseEntity<Long> getAppliedJobsCount(@RequestParam Long userId) {
        long count = jobApplicationRepo.countByUserId(userId);
        return ResponseEntity.ok(count);
    }

}
