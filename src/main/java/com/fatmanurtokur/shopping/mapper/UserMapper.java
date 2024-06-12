package com.fatmanurtokur.shopping.mapper;

import com.fatmanurtokur.shopping.dto.UserDto;
import com.fatmanurtokur.shopping.entity.Users;

public class UserMapper {

    public static UserDto mapToUserDto(Users users){
        return new UserDto(
                users.getUserId(),
                users.getUsername(),
                users.getPassword(),
                users.getEmail(),
                users.getFullName(),
                users.getAddress(),
                users.getPhone()
        );
    }

    public static Users mapToUsers(UserDto userDto){
        return new Users(
                userDto.getUserId(),
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getEmail(),
                userDto.getFullName(),
                userDto.getAddress(),
                userDto.getPhone()
        );
    }
}
