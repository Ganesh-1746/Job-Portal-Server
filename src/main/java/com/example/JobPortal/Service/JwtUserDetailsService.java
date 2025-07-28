package com.example.JobPortal.Service;

import com.example.JobPortal.Model.User;
import com.example.JobPortal.Repository.AdminRepo;
import com.example.JobPortal.Repository.UserRepo;
import com.example.JobPortal.Security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private AdminRepo adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(CustomUserDetails::new)
                .orElseGet(() ->
                        adminRepository.findByEmail(username)
                                .map(admin -> new CustomUserDetails(admin.getEmail(), admin.getPassword(), admin.getRole()))
                                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username))
                );
    }

}