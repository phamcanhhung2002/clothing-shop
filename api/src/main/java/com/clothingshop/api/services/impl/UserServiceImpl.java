package com.clothingshop.api.services.impl;

import com.clothingshop.api.domain.dtos.LoginDto;
import com.clothingshop.api.domain.dtos.LoginResponseDto;
import com.clothingshop.api.domain.dtos.MessageResponseDto;
import com.clothingshop.api.domain.dtos.UserResponseDto;
import com.clothingshop.api.domain.entities.RefreshTokenEntity;
import com.clothingshop.api.domain.entities.UserEntity;
import com.clothingshop.api.domain.enums.Role;
import com.clothingshop.api.exceptions.UserAlreadyExistsException;
import com.clothingshop.api.repositories.UserRepository;
import com.clothingshop.api.security.JwtUtils;
import com.clothingshop.api.security.services.RefreshTokenService;
import com.clothingshop.api.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Override
    public MessageResponseDto registerUser(UserEntity user) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Error: Email is already in use!");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(new Role[] { Role.ROLE_USER });

        userRepository.save(user);

        return new MessageResponseDto("User registered successfully!");
    }

    @Override
    public LoginResponseDto loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateJwtToken(authentication);

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userEntity.getId());

        return new LoginResponseDto(accessToken, refreshToken.getToken(),
                modelMapper.map(userEntity, UserResponseDto.class));
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public boolean isExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void updateUser(long id, String username, String fullName, String email, String phone, String address,
            String img) {
        userRepository.updateUser(id, username, fullName, email, phone, address, img);
    }
}
