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
public class UserDetailResponseDto {
    Long id;

    String username;

    String email;

    String fullName;

    Role[] roles;

    String img;

    String phone;

    String address;

    Date createdAt;

    Date updatedAt;
}
