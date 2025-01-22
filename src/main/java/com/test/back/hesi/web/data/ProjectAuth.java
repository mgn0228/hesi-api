package com.test.back.hesi.web.data;

import lombok.Getter;

@Getter
public enum ProjectAuth {
    ADMIN("ADMIN"),
    SAFETY("SAFETY"),
    USER("USER");

    /**
     * ADMIN : 메인회사
     * SAFETY : 안전기업
     * USER  : 협력사
     * Projects의 auth값이 ADMIN == 메인 회사의 프로젝트이다 == 메인 회사의 매니저들을 위한 프로젝트이다.
     * Projects의 auth값이 SAFETY == 안전기업 == 협력사가 결재 요청시 나타남(결재 권한이 있는 기업)
     * Projects의 auth값이 USER  == 다른 협력사의 프로젝트이다 == 협력사의 직원들을 위한 프로젝트이다.
     */

    private final String value;

    ProjectAuth(String value) {
        this.value = value;
    }
}
