package com.github.nmescv.departmenthr.security.dict;

public enum RoleDictionary {

    EMPLOYEE("EMPLOYEE"),
    BOSS("BOSS"),
    HR("HR");

    private final String role;

    RoleDictionary(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
