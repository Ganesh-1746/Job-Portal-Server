package com.example.JobPortal.Controller;

import com.example.JobPortal.Model.*;
import com.example.JobPortal.Model.DTO.*;
import com.example.JobPortal.Repository.CompanyRepo;
import com.example.JobPortal.Repository.JobApplicationRepo;
import com.example.JobPortal.Repository.JobRepo;
import com.example.JobPortal.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CompanyRepo companyRepository;

    @Autowired
    private JobRepo jobRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JobApplicationRepo jobApplicationRepo;

    @PostMapping("/company/register")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyRequestDTO dto) {
        Company company = new Company();
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());

        Company saved = companyRepository.save(company);
        return ResponseEntity.ok(saved);
    }


    @PostMapping("/addjob")
    public ResponseEntity<?> addJob(@RequestBody JobRequest dto) {
        Optional<Company> optionalCompany = companyRepository.findById(dto.getCompanyId());
        if (optionalCompany.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid company selected."));
        }

        Company company = optionalCompany.get();

        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setExperience(dto.getExperience());
        job.setLocation(dto.getLocation());
        job.setJobType(dto.getJobType());
        job.setDepartment(dto.getDepartment());
        job.setSalary(dto.getSalary());
        job.setPostedDate(dto.getPostedDate());
        job.setCompany(company); // 💡 Link company by ID

        jobRepository.save(job);
        return ResponseEntity.ok(Map.of("message", "Job added successfully."));
    }


    @GetMapping("/alljobs")
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();

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
                        job.getCompany().getName().trim().toUpperCase()// Flattened company name
                )
        ).toList();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/allUsers")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userRepo.findByRole(Role.ROLE_USER);
        System.out.println("Fetched users: " + users.size());
        List<UserResponseDTO> response = users.stream().map(user ->
                new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getMobileNo(),
                        user.getLocation(),
                        user.getRole(),
                        user.getResumeurl()
                )
        ).toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(user -> {
                    userRepo.delete(user);
                    return ResponseEntity.ok().body(
                            java.util.Map.of("message", "User deleted successfully"));
                })
                .orElse(ResponseEntity.status(404).body(
                        java.util.Map.of("message", "User not found")));
    }

    @GetMapping("/allCompanies")
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();

        List<CompanyResponseDTO> response = companies.stream()
                .map(c -> new CompanyResponseDTO(c.getId(), c.getName(), c.getDescription()))
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-job/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobUpdateDTO updatedJob) {
        Optional<Job> jobOpt = jobRepository.findById(id);

        if (jobOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Job not found"));
        }

        Optional<Company> companyOpt = companyRepository.findById(updatedJob.getCompanyId());
        if (companyOpt.isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("message", "Company not found"));
        }

        Job job = jobOpt.get();
        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setExperience(updatedJob.getExperience());
        job.setLocation(updatedJob.getLocation());
        job.setJobType(updatedJob.getJobType());
        job.setDepartment(updatedJob.getDepartment());
        job.setSalary(updatedJob.getSalary());
        job.setPostedDate(updatedJob.getPostedDate());
        job.setCompany(companyOpt.get()); // ✅ Update company here

        jobRepository.save(job);

        return ResponseEntity.ok(Map.of("message", "Job updated successfully"));
    }


    @DeleteMapping("/delete-job/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        Optional<Job> jobOpt = jobRepository.findById(id);

        if (jobOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Job not found"));
        }

        jobRepository.delete(jobOpt.get());

        return ResponseEntity.ok(Map.of("message", "Job deleted successfully"));
    }

    @GetMapping("/all-Applications")
    public ResponseEntity<List<JobApplyDTO>> getAllApplications() {
        List<JobApplication> applications = jobApplicationRepo.findAll();

        List<JobApplyDTO> response = applications.stream().map(app ->
                new JobApplyDTO(
                        app.getId(),
                        app.getUser().getName(),
                        app.getUser().getEmail(),
                        app.getJob().getTitle(),
                        app.getJob().getCompany().getName(),
                        app.getStatus(),
                        app.getAppliedDate()
                )
        ).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/Application/{id}")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long id,
                                                     @RequestBody UpdateApplicationStatusDTO statusDTO) {
        Optional<JobApplication> applicationOpt = jobApplicationRepo.findById(id);
        if (applicationOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Application not found"));
        }

        JobApplication application = applicationOpt.get();
        application.setStatus(statusDTO.getStatus());
        jobApplicationRepo.save(application);

        return ResponseEntity.ok(Map.of("message", "Application status updated successfully"));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        long userCount = userRepo.count();
        long companyCount = companyRepository.count();
        long jobCount = jobRepository.count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("users", userCount);
        stats.put("companies", companyCount);
        stats.put("jobs", jobCount);

        return ResponseEntity.ok(stats);
    }

}
