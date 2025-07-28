package com.example.JobPortal.Service;

import com.example.JobPortal.Model.RefreshToken;
import com.example.JobPortal.Model.User;
import com.example.JobPortal.Repository.RefreshTokenRepo;
import com.example.JobPortal.Repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${JWT.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepo repo;
    @Autowired
    private UserRepo userRepo;

    @Transactional
    public RefreshToken create(String username) {
        Optional<User> user = userRepo.findByEmail(username);
        repo.deleteByUser(user);
        RefreshToken rt = new RefreshToken();
        rt.setUser(user.orElse(null));
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        return repo.save(rt);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            repo.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repo.findByToken(token);
    }

    public void deleteByUserId(Long userId) {
        Optional<User> user = userRepo.findById(userId);
        repo.deleteByUser(user);
    }
}

