package com.fatmanurtokur.shopping.util;

import com.fatmanurtokur.shopping.repository.RolesRepository;
import com.fatmanurtokur.shopping.repository.UserRepository;
import com.fatmanurtokur.shopping.repository.UserRolesRepository;
import com.fatmanurtokur.shopping.service.impl.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private static AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        SecurityUtil.adminService = adminService;
    }

    public static boolean isAdmin(Authentication authentication) {
        String username = authentication.getName();
        Long userId = adminService.getUserIdByUsername(username);
        return adminService.isAdmin(userId);
    }
}
