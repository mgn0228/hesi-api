package com.test.back.hesi.web.model.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class LoginHistory {
    @Id
    //@ApiModelProperty(value = "몽고 식별자")
    private String docId;

    //@ApiModelProperty("입력 아이디")
    private String userId;

    //@ApiModelProperty("입력 비밀번호")
    private String password;

    //@ApiModelProperty("성공 여부")
    private boolean isSuccess;

    //@ApiModelProperty("요청일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDt;

    //@ApiModelProperty("접근 정보")
    private String platformInfo;

    @Builder
    public LoginHistory(String docId, String userId, String password, boolean isSuccess,
                        LocalDateTime createDt, String platformInfo) {
        super();
        this.docId = docId;
        this.userId = userId;
        this.password = password;
        this.isSuccess = isSuccess;
        this.createDt = createDt;
        this.platformInfo = platformInfo;
    }
}
