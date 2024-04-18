package com.clothingshop.api.controllers;

import com.clothingshop.api.domain.dtos.UserDetailResponseDto;
import com.clothingshop.api.domain.dtos.UserDto;
import com.clothingshop.api.domain.dtos.UserResponseDto;
import com.clothingshop.api.domain.entities.UserEntity;
import com.clothingshop.api.exceptions.UserAlreadyExistsException;
import com.clothingshop.api.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDto> listUsers(Pageable pageable, Sort sort) {
        Page<UserEntity> users = userService
                .findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
        return users.map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDetailResponseDto> findUser(@PathVariable Long id) {
        Optional<UserEntity> foundUser = userService.findById(id);
        return foundUser.map(bookEntity -> {
            UserDetailResponseDto userDto = modelMapper.map(bookEntity, UserDetailResponseDto.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserDto userDto) {
        try {
            UserEntity user = modelMapper.map(userDto, UserEntity.class);
            return new ResponseEntity<>(modelMapper.map(userService.registerUser(user), UserResponseDto.class),
                    HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        if (!userService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        userService.updateUser(id, userDto.getUsername(), userDto.getFullName(),
                userDto.getEmail(), userDto.getPhone(), userDto.getAddress(), userDto.getImg());
        return new ResponseEntity<>(
                null,
                HttpStatus.OK);
    }
}
