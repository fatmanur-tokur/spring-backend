package com.fatmanurtokur.shopping.service.impl;


import com.fatmanurtokur.shopping.entity.UserRoles;
import com.fatmanurtokur.shopping.entity.Users;
import com.fatmanurtokur.shopping.repository.UserRepository;
import com.fatmanurtokur.shopping.repository.UserRolesRepository;
import com.fatmanurtokur.shopping.service.IAdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService implements IAdminService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;


    @Override
    public Users getUserDetails(Long userId){
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<UserRoles> getUserRoles(Long userId) {
        return userRolesRepository.findAllByUser_UserId(userId);
    }

    public boolean isAdmin(Long userId) {
        List<UserRoles> userRoles = userRolesRepository.findAllByUser_UserId(userId);
        return userRoles.stream()
                .anyMatch(userRole -> userRole.getRole().getRoleName().equals("ADMIN"));
    }

    public Long getUserIdByUsername(String username) {
        Users user = userRepository.findByUsername(username);
        return user != null ? user.getUserId() : null;
    }
}
