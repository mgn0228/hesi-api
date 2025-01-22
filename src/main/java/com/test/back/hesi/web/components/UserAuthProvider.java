package com.test.back.hesi.web.components;

import com.test.back.hesi.web.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserAuthProvider implements AuthenticationProvider {

    private final OAuthService oAuthService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String password = (String)authentication.getCredentials();

        // 익명 사용자 처리
        if (id == null || id.equalsIgnoreCase("anonymousUser")) {
            return null; // 인증하지 않음
        }

        UserDetails userDetails = oAuthService.loadUserByUsername(id);
        if (Objects.isNull(userDetails)) {
            throw new BadCredentialsException("username is not found. username = " + id);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
