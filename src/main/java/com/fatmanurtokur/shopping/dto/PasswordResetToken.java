package com.fatmanurtokur.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordResetToken {
    private String code;
    private long timestamp;
}
