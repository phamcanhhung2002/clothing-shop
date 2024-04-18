package com.clothingshop.api.domain.dtos;

import com.clothingshop.api.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String img;

    private Role[] roles;

    private Date createdAt;

    private Date updatedAt;
}
