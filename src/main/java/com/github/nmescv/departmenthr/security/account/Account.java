package com.github.nmescv.departmenthr.security.account;

import com.github.nmescv.departmenthr.security.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Account implements UserDetails {

    private final String tabelNumber;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final Boolean isActive;

    public Account(User user) {
        this.tabelNumber = user.getEmployee().getTabelNumber();
        this.password = user.getPassword();
        this.authorities = getAuthorities(user);
        this.isActive = user.getStatus().getName().equals("ACTIVE");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getUsername() {
        return tabelNumber;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmployee().getTabelNumber(),
                user.getPassword(),
                user.getStatus().getName().equals("ACTIVE"),
                user.getStatus().getName().equals("ACTIVE"),
                user.getStatus().getName().equals("ACTIVE"),
                user.getStatus().getName().equals("ACTIVE"),
                getAuthorities(user));
    }

    private static List<SimpleGrantedAuthority> getAuthorities(User user) {
        return user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
