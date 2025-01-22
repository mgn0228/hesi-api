package com.test.back.hesi.web.controller;

import com.test.back.hesi.utils.ResponseUtil;
import com.test.back.hesi.utils.UserAuthUtil;
import com.test.back.hesi.web.model.entity.Users;
import com.test.back.hesi.web.service.ProjectService;
import com.test.back.hesi.web.service.cmmn.UserJwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
//@Api(tags = {"Project"}, description = "프로젝트", basePath = "/")
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final UserJwtService userJwtService;
    private final UserAuthUtil userAuthUtil;

    @Autowired
    public ProjectController(ProjectService projectService, UserJwtService userJwtService, UserAuthUtil userAuthUtil) {
        this.projectService = projectService;
        this.userJwtService = userJwtService;
        this.userAuthUtil = userAuthUtil;
    }

    @GetMapping(value = "/list/{company-id}")
    //@ApiOperation(value = "전체 목록 조회 (다건)", notes = "ADMIN, USER 조건 있음")
    //@PathVariable("company-id") @ApiParam(value = "회사(company) 기본키", required = true) Long companyId
    public ResponseEntity<?> findAll(
            HttpServletRequest httpServletRequest,
            @PathVariable("company-id") Long companyId) throws Exception {
        Users user = userJwtService.getUserInfoByToken(httpServletRequest);
        return ResponseUtil.sendResponse( projectService.findProjectList(user) );
    }
}
