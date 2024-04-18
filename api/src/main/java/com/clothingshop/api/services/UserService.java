package com.clothingshop.api.services;

import com.clothingshop.api.domain.dtos.LoginDto;
import com.clothingshop.api.domain.dtos.LoginResponseDto;
import com.clothingshop.api.domain.dtos.MessageResponseDto;
import com.clothingshop.api.domain.entities.UserEntity;
import com.clothingshop.api.exceptions.UserAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    MessageResponseDto registerUser(UserEntity user) throws UserAlreadyExistsException;

    LoginResponseDto loginUser(LoginDto loginDto);

    Optional<UserEntity> findById(Long userId);

    Page<UserEntity> findAll(Pageable pageable);

    boolean isExists(Long id);

    void updateUser(long id, String username, String fullName, String email, String phone, String address, String img);
}
