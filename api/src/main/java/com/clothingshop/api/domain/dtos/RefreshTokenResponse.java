package com.clothingshop.api.domain.dtos;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    String accessToken;
    String refreshToken;
    String tokenType = "Bearer";

    public RefreshTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
