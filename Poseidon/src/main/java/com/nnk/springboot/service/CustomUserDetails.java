package com.nnk.springboot.service;

import com.nnk.springboot.Interface.DisplayName;
import com.nnk.springboot.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetails implements UserDetails, DisplayName {
    @Override
    public String getDisplayName() {
        return appUser.getFullname() != null ? appUser.getFullname() : appUser.getUsername();
    }

    private final User appUser;

    public CustomUserDetails(User appUser) {
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                () -> "ROLE_" + appUser.getRole().toUpperCase());
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getCustomProperty(int i) {
        String[] properties = {
                appUser.getUsername(),
                appUser.getFullname(),
                appUser.getRole(),
                "AdditionalProperty1",
                "AdditionalProperty2"
        };
        if (i >= 0 && i < properties.length) {
            return properties[i];
        }
        return "Not found";
    }

    public User getAppUser() {
        return appUser;
    }


}