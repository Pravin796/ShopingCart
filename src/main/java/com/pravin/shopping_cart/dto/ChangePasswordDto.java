package com.pravin.shopping_cart.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
