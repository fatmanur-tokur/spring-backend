package com.fatmanurtokur.shopping.controller;

import com.fatmanurtokur.shopping.config.JwtTokenProvider;
import com.fatmanurtokur.shopping.dto.PasswordChangeDto;
import com.fatmanurtokur.shopping.dto.PasswordResetDto;
import com.fatmanurtokur.shopping.dto.UserDto;
import com.fatmanurtokur.shopping.enums.ErrorEnum;
import com.fatmanurtokur.shopping.service.impl.EmailService;
import com.fatmanurtokur.shopping.service.impl.UserService;
import com.fatmanurtokur.shopping.validate.UserLoginValidate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "UserController", description = "User Controller Endpoints")
public class UserController {

    private UserService userService;
    private EmailService emailService;

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

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(Authentication authentication){
        String username = authentication.getName();
        UserDto userDto = userService.getUserByUsername(username);
        if(userDto==null){
            return new ResponseEntity<>(ErrorEnum.USER_NOT_FOUND.getMsg(),HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(Authentication authentication,@RequestBody PasswordChangeDto passwordChangeDto){
        boolean isChanged = userService.changePassword(authentication.getName(), passwordChangeDto);
        if(!isChanged){
            return new ResponseEntity<>(ErrorEnum.INVALID_PASSWORD.getMsg(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/resetRequest")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        String code = userService.generatePasswordResetCode(email);
        if (code == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        emailService.sendEmail(email, code);
        return new ResponseEntity<>("Reset code sent to email", HttpStatus.OK);
    }

    @PostMapping("/resetConfirm")
    public ResponseEntity<String> confirmPasswordReset(@RequestBody PasswordResetDto request) {
        boolean result = userService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        if (!result) {
            return new ResponseEntity<>("Invalid code or code expired", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }
}
