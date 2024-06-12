package com.fatmanurtokur.shopping.service.impl;

import com.fatmanurtokur.shopping.config.JwtTokenProvider;
import com.fatmanurtokur.shopping.dto.UserDto;
import com.fatmanurtokur.shopping.entity.Users;
import com.fatmanurtokur.shopping.mapper.UserMapper;
import com.fatmanurtokur.shopping.repository.UserRepository;
import com.fatmanurtokur.shopping.service.IUserService;
import com.fatmanurtokur.shopping.validate.UserLoginValidate;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername()) || userRepository.existsByEmail(userDto.getEmail())) {
            return null;
        }
        Users users = UserMapper.mapToUsers(userDto);
        users.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Users savedUsers = userRepository.save(users);
        return UserMapper.mapToUserDto(savedUsers);

    }

    @Override
    public String login(UserLoginValidate loginValidate){

        Users user = userRepository.findByEmail(loginValidate.getEmail());

        if (user == null) {
            return null;
        }
        if(!passwordEncoder.matches(loginValidate.getPassword(), user.getPassword())){
            return null;
        }
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
        return jwtTokenProvider.generateToken(authentication);


    }
}
