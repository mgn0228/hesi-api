package com.test.back.hesi.web.service;

import com.test.back.hesi.web.dto.request.RequestUserDTO;
import com.test.back.hesi.web.model.cmmn.BfToken;
import com.test.back.hesi.web.model.cmmn.service.BfCRUDService;
import com.test.back.hesi.web.model.entity.Users;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends BfCRUDService<Users> {
    BfToken login(Users user, HttpServletRequest httpServletRequest) throws Exception;
    boolean checkUserId(String userId);
    Users findByEmailAndUserNameAndDeleteYnFalse(String email, String userName);
    boolean findByUserIdAndEmailAndUserNameAndDeleteYnFalse(String userId, String email, String userName);
    Users modifyPassword(String password, String newpassword, HttpServletRequest httpServletRequest) throws Exception;
    Users modifyPasswordNotLogin(String userId, String newpassword, HttpServletRequest httpServletRequest) throws Exception;
    Boolean getEmailDuplicate(String email);
    @Transactional
    void add(RequestUserDTO dto) throws Exception;
}