package com.test.back.hesi.web.dto.request;

//import io.swagger.v3.oas.annotations.media.Schema;
import com.test.back.hesi.web.data.SNSType;
import com.test.back.hesi.web.data.UserType;
import com.test.back.hesi.web.data.YN;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.test.back.hesi.web.data.SNSType.NORMAL;

@Getter
@Setter
//@Schema(description = "유저")
public class RequestUserDTO {
    //@Schema(description = "타입")
    UserType type;

    //@Schema(description = "유저ID")
    String userId;

    //@Schema(description = "핸드폰번호")
    String phoneNo;

    //@Schema(description = "유저 이름")
    String userName;

    //@Schema(description = "이메일주소")
    String email;

    //@Schema(description = "SNS동의여부")
    YN snsAllowed;

    //@Schema(description = "마케팅유무")
    YN marketingAllowed;

    //@Schema(description = "마케팅유무시간")
    LocalDateTime marketingAllowedAt;

    //@Schema(description = "메시지동의여부")
    YN messageAllowed;

    //@Schema(description = "메시지동의시각")
    LocalDateTime messageAllowedAt;

    //@Schema(description = "비밀번호")
    String password;

    //@Schema(description = "SNS타입")
    SNSType snsType = NORMAL;

    //@Schema(description = "회사")
    Long company;

    //@Schema(description = "프로젝트")
    Long project;
}
