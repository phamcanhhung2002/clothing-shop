package com.clothingshop.api.security.services.impl;

import com.clothingshop.api.domain.entities.RefreshTokenEntity;
import com.clothingshop.api.exceptions.TokenRefreshException;
import com.clothingshop.api.repositories.RefreshTokenRepository;
import com.clothingshop.api.repositories.UserRepository;
import com.clothingshop.api.security.services.RefreshTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiryInMils}")
    int refreshTokenExpiryInMils;

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Override
    public RefreshTokenEntity createRefreshToken(Long userId) {
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder().user(userRepository.findById(userId).get())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiryInMils))
                .token(UUID.randomUUID().toString()).build();

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public int deleteByToken(String token) {
        return refreshTokenRepository.deleteByToken(token);
    }
}
