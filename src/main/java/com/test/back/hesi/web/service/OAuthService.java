package com.test.back.hesi.web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface OAuthService extends UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username);
}
