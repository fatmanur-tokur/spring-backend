package com.fatmanurtokur.shopping.enums;

public enum RoleEnum {
    ADMIN(2),
    USER(1);

    private final int roleId;

    RoleEnum(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
}
