package com.example.JobPortal.Service;

import com.example.JobPortal.Model.Role;
import com.example.JobPortal.Model.User;
import com.example.JobPortal.Repository.UserRepo;
import com.example.JobPortal.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public List<User> getProfiles() {
        return userRepository.findAll();
    }

    public void Registration(User user) {
        userRepository.save(user);
    }

    public UserService(UserRepo userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(String name, String email,String mobileNo,String location, String password, String resumeUrl) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        else {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setMobileNo(mobileNo);
            user.setLocation(location);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.ROLE_USER);
            user.setResumeurl(resumeUrl);
            return userRepository.save(user);
        }
    }

    public String login(String email, String password, String requiredRole)
            throws InvalidEmailException, InvalidPasswordException, AccessDeniedException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidEmailException("Invalid email"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        String expectedRole = requiredRole.toUpperCase();
        if (!expectedRole.startsWith("ROLE_")) {
            expectedRole = "ROLE_" + expectedRole;
        }

        if (!user.getRole().name().equals(expectedRole)) {
            throw new AccessDeniedException("Access denied: not a " + user.getRole().name());
        }

        return jwtUtil.generateToken(email,user.getRole());
    }


    public class InvalidEmailException extends RuntimeException {
        public InvalidEmailException(String message) {
            super(message);
        }
    }

    public class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }
}






