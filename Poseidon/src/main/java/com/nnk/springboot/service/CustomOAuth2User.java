package com.nnk.springboot.service;

import com.nnk.springboot.Interface.DisplayName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User, DisplayName{
    private final OAuth2User oauth2User;
    private final String provider;
    public CustomOAuth2User(OAuth2User oauth2User,String provider) {
        this.oauth2User = oauth2User;
        this.provider = provider;
    }
    public String getProvider() {
        return provider;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return String.valueOf(oauth2User.getAttributes().get("name"));
    }

    @Override
    public String getDisplayName() {
        return oauth2User.getAttributes().get("name") != null
                ? String.valueOf(oauth2User.getAttributes().get("name"))
                : String.valueOf(oauth2User.getAttributes().get("login"));
    }
}
