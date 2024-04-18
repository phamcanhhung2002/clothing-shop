package com.clothingshop.api.security.services;

import com.clothingshop.api.domain.entities.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshTokenEntity> findByToken(String token);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);

    RefreshTokenEntity createRefreshToken(Long id);

    int deleteByToken(String token);
}
