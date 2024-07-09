package com.fatmanurtokur.shopping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Schema(hidden = true)
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String phone;
}
