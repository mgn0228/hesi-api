package com.test.back.hesi.web.controller;

import com.test.back.hesi.utils.ResponseUtil;
import com.test.back.hesi.web.dto.request.RequestUserDTO;
import com.test.back.hesi.web.service.ProjectService;
import com.test.back.hesi.web.service.UserService;
import com.test.back.hesi.web.service.cmmn.UserJwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Api(tags = {"User"})
public class UsersController extends BfAbsUserController{

    private final ProjectService projectService;

    @Autowired
    public UsersController(UserService userService,
                           UserJwtService userJwtService,
                           ProjectService projectService) {
        super(userService, userJwtService);
        this.projectService = projectService;
    }

    @PostMapping(value = "/user/login")
    //@ApiOperation(value = "사용자 로그인+토큰 발급", notes = "사용자 로그인+토큰 발급")
    public ResponseEntity<?> login(@RequestBody RequestUserDTO dto,
                                   HttpServletRequest request) throws Exception{
        return super.login(dto.getUserId(), dto.getPassword(), request);
    }

    @GetMapping(value = "/user/check/not-expired")
    //@ApiOperation(value = "토큰 만료여부 체크", notes = "토큰 만료여부 체크")
    public ResponseEntity<?> checkTokenExpired(HttpServletRequest request) throws Exception{
        return ResponseUtil.sendResponse(!userJwtService.checkTokenExpired(request));
    }
}
