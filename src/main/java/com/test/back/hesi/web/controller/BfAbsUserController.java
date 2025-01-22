package com.test.back.hesi.web.controller;

import com.test.back.hesi.utils.ResponseUtil;
import com.test.back.hesi.web.model.entity.Users;
import com.test.back.hesi.web.service.UserService;
import com.test.back.hesi.web.service.cmmn.UserJwtService;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public abstract class BfAbsUserController {

    protected final UserService userService;

    protected final UserJwtService userJwtService;

    public BfAbsUserController(UserService userService, UserJwtService userJwtService) {
        this.userService = userService;
        this.userJwtService = userJwtService;
    }

    public ResponseEntity login(String username, String password, HttpServletRequest request) throws Exception {
        Users user = new Users();
        user.setUserId(username);
        user.setPassword(password);

        return ResponseUtil.sendResponse(userService.login(user, request));
    }

    public ResponseEntity checkDuplicated(String userId) throws Exception {
        return ResponseUtil.sendResponse(userService.checkUserId(userId));
    }
}
