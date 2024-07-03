package com.fatmanurtokur.shopping.service;

import com.fatmanurtokur.shopping.dto.PasswordChangeDto;
import com.fatmanurtokur.shopping.dto.UserDto;
import com.fatmanurtokur.shopping.validate.UserLoginValidate;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {
    UserDto createUser(UserDto userDto);

    String login(UserLoginValidate loginValidate);

    UserDto getUserByUsername(String username);

    UserDetails loadUserByUsername(String username);

    boolean changePassword(String username, PasswordChangeDto passwordChangeDto);

    UserDto getUserByEmail(String email);

    String generatePasswordResetCode(String email);

    boolean resetPassword(String email, String code, String newPassword);
}
