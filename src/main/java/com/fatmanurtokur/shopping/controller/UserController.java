package com.fatmanurtokur.shopping.controller;

import com.fatmanurtokur.shopping.config.JwtTokenProvider;
import com.fatmanurtokur.shopping.dto.UserDto;
import com.fatmanurtokur.shopping.enums.ErrorEnum;
import com.fatmanurtokur.shopping.service.impl.UserService;
import com.fatmanurtokur.shopping.validate.UserLoginValidate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "UserController", description = "User Controller Endpoints")
public class UserController {

    private UserService userService;
    private JwtTokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto){
        UserDto savedUser= userService.createUser(userDto);
        if (savedUser == null) {
            return new ResponseEntity<>(ErrorEnum.USER_ALREADY_EXISTS.getMsg(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginValidate loginValidate) {
        String token = userService.login(loginValidate);
        if (token == null) {
            return new ResponseEntity<>(ErrorEnum.USER_NOT_FOUND.getMsg(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(token);
    }
}
