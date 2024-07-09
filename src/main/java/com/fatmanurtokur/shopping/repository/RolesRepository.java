package com.fatmanurtokur.shopping.repository;

import com.fatmanurtokur.shopping.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Roles findByRoleName(String roleName);
}
