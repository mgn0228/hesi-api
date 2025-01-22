package com.test.back.hesi.web.service.cmmn;


import com.test.back.hesi.web.data.UserType;
import com.test.back.hesi.web.model.cmmn.BfToken;
import com.test.back.hesi.web.model.entity.Users;
import jakarta.servlet.http.HttpServletRequest;

public interface UserJwtService {
    Users getUserInfoByToken(HttpServletRequest httpServletRequest);
    
    Users getUserInfoByTokenAnyway(HttpServletRequest httpServletRequest);
    
    boolean checkUserTokenPriority(HttpServletRequest httpServletRequest, UserType lessType);

	BfToken generateToken(Users user, String userCode);
    
    boolean checkTokenExpired(HttpServletRequest httpServletRequest);

    BfToken generateTokenByRefreshToken(String refreshToken);
}
