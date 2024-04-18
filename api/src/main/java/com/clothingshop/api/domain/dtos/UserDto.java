package com.clothingshop.api.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    Long id;

    @NotBlank
    String username;

    String password;

    String fullName;

    @Email
    String email;

    Boolean isAdmin;

    String phone;

    String address;

    String img;
}
