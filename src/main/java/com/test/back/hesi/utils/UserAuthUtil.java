package com.test.back.hesi.utils;

import com.test.back.hesi.web.data.ProjectAuth;
import com.test.back.hesi.web.model.entity.Users;
import com.test.back.hesi.web.repos.direct.DirectQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserAuthUtil {
    private final DirectQuery directQuery;

    /**
     * Users를 이용하여 자신이 속한 프로젝트의 권한을 조회
     * @param user 접속 계정의 Users
     * @return ADMIN(관리자), SAFETY(안전기업), USER(협력사)
     */
    public String getProjectAuth(Users user) {
        Map<String, Object> map = directQuery.findUserProjectAuth(user.getId());

        int adminCnt = Integer.parseInt(String.valueOf(map.get("adminCnt")));
        int safetyCnt = Integer.parseInt(String.valueOf(map.get("safetyCnt")));

        if(adminCnt > 0) {
            return ProjectAuth.ADMIN.getValue();
        }else if(safetyCnt > 0) {
            return ProjectAuth.SAFETY.getValue();
        }else {
            return ProjectAuth.USER.getValue();
        }
    }
}
