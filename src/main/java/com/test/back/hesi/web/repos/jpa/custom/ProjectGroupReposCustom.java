package com.test.back.hesi.web.repos.jpa.custom;

import com.test.back.hesi.web.data.UserAuthType;
import com.test.back.hesi.web.dto.response.ResponseProjectMember;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectGroupReposCustom {

    @Transactional(readOnly = true)
    List<ResponseProjectMember> findByProjectId(
            Long projectId,
            String keyword,
            Boolean orderbyName,
            Boolean orderbyEmail,
            UserAuthType userType,
            Integer page,
            Integer size,
            String auth
    );

    List<ResponseProjectMember> findByPage(Long projectId, String keyword, Boolean orderbyName, Boolean orderbyEmail, UserAuthType userType, Integer page, Integer size, String auth);

    Long getTotalCount(Long projectId, String keyword, UserAuthType userType, String auth);
}
