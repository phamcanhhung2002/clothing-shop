package com.clothingshop.api.controllers;

import com.clothingshop.api.domain.dtos.*;
import com.clothingshop.api.domain.entities.RefreshTokenEntity;
import com.clothingshop.api.domain.entities.UserEntity;
import com.clothingshop.api.exceptions.TokenRefreshException;
import com.clothingshop.api.exceptions.UserAlreadyExistsException;
import com.clothingshop.api.security.JwtUtils;
import com.clothingshop.api.security.services.RefreshTokenService;
import com.clothingshop.api.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    ModelMapper userMapper;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> registerUser(@Valid @RequestBody UserDto userDto) {
        try {
            UserEntity user = userMapper.map(userDto, UserEntity.class);
            MessageResponseDto messageResponseDto = userService.registerUser(user);
            return new ResponseEntity<>(messageResponseDto, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(new MessageResponseDto(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto) throws Exception {
        LoginResponseDto loginResponseDto = userService.loginUser(loginDto);

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        String requestRefreshToken = refreshTokenDto.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteByToken(refreshTokenDto.getRefreshToken());
        return ResponseEntity.ok(new MessageResponseDto("Log out successful!"));
    }
}
