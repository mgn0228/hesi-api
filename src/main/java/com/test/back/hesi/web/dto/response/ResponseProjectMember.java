package com.test.back.hesi.web.dto.response;

import com.test.back.hesi.web.data.UserAuthType;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Schema(description = "프로젝트 그룹원")
public class ResponseProjectMember {
    //@Schema(description = "유저아이디")
    Long id;

    //@Schema(description = "그룹원 이름")
    String userName;

    //@Schema(description = "이메일 주소")
    String email;

    //@Schema(description = "프로필 이미지 주소")
    String image;

    //@Schema(description = "권한")
    UserAuthType userAuthType;
}
