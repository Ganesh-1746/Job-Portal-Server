package com.example.JobPortal.Service;

import com.example.JobPortal.Model.Admin;
import com.example.JobPortal.Repository.AdminRepo;
import com.example.JobPortal.Security.JwtUtil;
import com.example.JobPortal.Controller.AuthController.InvalidEmailException;
import com.example.JobPortal.Controller.AuthController.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String email, String password) {
        Admin admin = adminRepo.findByEmail(email)
                .orElseThrow(() -> new InvalidEmailException("Invalid admin email"));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new InvalidPasswordException("Invalid admin password");
        }

        return jwtUtil.generateToken(admin.getEmail(), admin.getRole());
    }
}
