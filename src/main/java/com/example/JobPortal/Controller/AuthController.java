package com.example.JobPortal.Controller;

import com.example.JobPortal.Model.RefreshToken;
import com.example.JobPortal.Model.User;
import com.example.JobPortal.Repository.UserRepo;
import com.example.JobPortal.Request.LoginRequest;
import com.example.JobPortal.Request.TokenRefreshRequest;
import com.example.JobPortal.Response.ErrorResponse;
import com.example.JobPortal.Response.LoginResponse;
import com.example.JobPortal.Response.RegisterResponse;
import com.example.JobPortal.Security.JwtUtil;
import com.example.JobPortal.Service.AdminService;
import com.example.JobPortal.Service.MinioService;
import com.example.JobPortal.Service.RefreshTokenService;
import com.example.JobPortal.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private MinioService minioService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;


    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String mobileNo,
                                      @RequestParam String location,
                                      @RequestParam String password,
                                      @RequestParam(required = false) MultipartFile resume) {
        try {
            String resumeUrl = null;

            if (resume != null && !resume.isEmpty()) {
                resumeUrl = minioService.uploadFile(resume);
            }

            User savedUser = userService.register(name, email, mobileNo, location, password, resumeUrl);

            RegisterResponse response = new RegisterResponse(
                    "User registered successfully",
                    savedUser.getEmail(),
                    savedUser.getName(),
                    savedUser.getResumeurl()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return processUserLogin(loginRequest);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return processAdminLogin(loginRequest);
    }

    private ResponseEntity<?> processUserLogin(LoginRequest loginRequest) {
        try {
            String token = userService.login(
                    loginRequest.getEmail(),
                    loginRequest.getPassword(),
                    "ROLE_USER"
            );
            RefreshToken rt = refreshTokenService.create(loginRequest.getEmail());
            return ResponseEntity.ok().body(new LoginResponse(token,rt.getToken(), "Login successful as USER"));

        } catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid email"));
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid password"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Access denied"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Something went wrong"));
        }
    }

    private ResponseEntity<?> processAdminLogin(LoginRequest loginRequest) {
        try {
            String token = adminService.login(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            RefreshToken rt = refreshTokenService.create(loginRequest.getEmail());
            return ResponseEntity.ok().body(new LoginResponse(token,rt.getToken(), "Login successful as ADMIN"));

        } catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid admin email"));
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid password"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Something went wrong"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || !jwtUtil.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }

        String email = jwtUtil.getUserEmailFromToken(refreshToken);


        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());


        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", refreshToken);

        return ResponseEntity.ok(response);
    }

    public static class InvalidEmailException extends RuntimeException {
        public InvalidEmailException(String message) {
            super(message);
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }
}
