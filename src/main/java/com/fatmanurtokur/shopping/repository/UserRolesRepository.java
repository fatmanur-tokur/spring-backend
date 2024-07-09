package com.fatmanurtokur.shopping.repository;

import com.fatmanurtokur.shopping.entity.Roles;
import com.fatmanurtokur.shopping.entity.UserRoles;
import com.fatmanurtokur.shopping.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    List<UserRoles> findAllByUser_UserId(Long userId);
    UserRoles findByUserAndRole(Users user, Roles role);

}
