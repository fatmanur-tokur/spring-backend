package com.fatmanurtokur.shopping.service;

import com.fatmanurtokur.shopping.entity.UserRoles;
import com.fatmanurtokur.shopping.entity.Users;

import java.util.List;

public interface IAdminService {
    Users getUserDetails(Long userId);

    List<UserRoles> getUserRoles(Long userId);
}
