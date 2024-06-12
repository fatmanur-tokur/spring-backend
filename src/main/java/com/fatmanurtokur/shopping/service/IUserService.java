package com.fatmanurtokur.shopping.service;

import com.fatmanurtokur.shopping.dto.UserDto;
import com.fatmanurtokur.shopping.validate.UserLoginValidate;

public interface IUserService {
    UserDto createUser(UserDto userDto);

    String login(UserLoginValidate loginValidate);
}
