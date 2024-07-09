package com.fatmanurtokur.shopping.controller;

import com.fatmanurtokur.shopping.dto.UserDetailsFADto;
import com.fatmanurtokur.shopping.dto.UserDto;
import com.fatmanurtokur.shopping.entity.UserRoles;
import com.fatmanurtokur.shopping.entity.Users;
import com.fatmanurtokur.shopping.enums.ErrorEnum;
import com.fatmanurtokur.shopping.service.IAdminService;
import com.fatmanurtokur.shopping.service.impl.AdminService;
import com.fatmanurtokur.shopping.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
@Tag(name = "AdminController", description = "Admin Controller Endpoints")
public class AdminController {

    private final IAdminService adminService;

    @GetMapping("/userDetails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getUserDetails(@RequestParam Long userId,Authentication authentication) {

        if (!SecurityUtil.isAdmin(authentication)) {
            return new ResponseEntity<>(ErrorEnum.ACCESS_DENIED.getMsg(), HttpStatus.FORBIDDEN);        }

        Users user = adminService.getUserDetails(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserDetailsFADto userDetailsFADto = new UserDetailsFADto();
        userDetailsFADto.setUsername(user.getUsername());
        userDetailsFADto.setEmail(user.getEmail());
        userDetailsFADto.setFullName(user.getFullName());
        userDetailsFADto.setAddress(user.getAddress());
        userDetailsFADto.setPhone(user.getPhone());

        return ResponseEntity.ok(userDetailsFADto);
    }

}
