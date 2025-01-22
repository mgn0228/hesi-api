package com.test.back.hesi.web.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.test.back.hesi.web.data.SNSType;
import com.test.back.hesi.web.data.UserType;
import com.test.back.hesi.web.data.YN;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.test.back.hesi.web.data.SNSType.NORMAL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity(name = "users")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private UserType type;

    @Column(name = "sns_allowed")
    private YN snsAllowed;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "marketing_allowed")
    private YN marketingAllowed;

    @Column(name = "marketing_allowed_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime marketingAllowedAt;

    @Column(name = "message_allowed")
    private YN messageAllowed;

    @Column(name = "message_allowed_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime messageAllowedAt;

    @Column(name = "email_allowed", columnDefinition = "bit(1) COMMENT '유저 이메일 수신 알림 동의여부'")
    private Boolean emailAllowed = true;

    @Column(name = "image")
    private String image;

    @Column(name = "password")
    private String password;

    @Column(name = "deleted")
    private YN deleted;

    @Enumerated(STRING)
    @Column(name = "sns_type")
    SNSType snsType = NORMAL;

    @Column(name = "sns_value")
    String snsValue;

    @Column(name = "max_project_group_count")
    private int maxProjectGroupCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "company", columnDefinition = "bigint COMMENT '회사(소속) 기본키'")
    private Company company;

    @Column(name = "use_yn", columnDefinition = "bit(1) COMMENT '활성화/비활성화' default 1", nullable = false)
    private Boolean useYn = true;

    // 자식 테이블 맵핑
    //@OneToMany(mappedBy = "user")
    //private List<UserAuth> userAuthList = new ArrayList<>();
    //@OneToMany(mappedBy = "user")
    //private List<AccidentExp> accidentExps = new ArrayList<>();
    //@OneToMany(mappedBy = "user")
    //private List<ChecklistProject> checklistProjectList = new ArrayList<>();
    
    public Users(Users users) {
        this.id = users.id;
        this.type = users.type;
        this.snsAllowed = users.snsAllowed;
        this.userId = users.userId;
        this.phoneNo = users.phoneNo;
        this.userName = users.userName;
        this.email = users.email;
        this.snsType = users.snsType;
        this.snsValue = users.snsValue;
        this.marketingAllowed = users.marketingAllowed;
        this.marketingAllowedAt = users.marketingAllowedAt;
        this.messageAllowed = users.messageAllowed;
        this.messageAllowedAt = users.messageAllowedAt;
        this.image = users.image;
        this.password = users.password;
        this.deleted = users.deleted;
        this.maxProjectGroupCount = users.maxProjectGroupCount;
    }

    @Builder
    public Users(long id, UserType type, YN snsAllowed, String userId, String phoneNo, String userName, String email,
                 YN marketingAllowed, LocalDateTime marketingAllowedAt, YN messageAllowed, LocalDateTime messageAllowedAt,
                 String image, String password, int maxProjectGroupCount, Company company) {
        
        this.id = id;
        this.type = type;
        this.snsAllowed = snsAllowed;
        this.userId = userId;
        this.phoneNo = phoneNo;
        this.userName = userName;
        this.email = email;
        this.marketingAllowed = marketingAllowed;
        this.marketingAllowedAt = marketingAllowedAt;
        this.messageAllowed = messageAllowed;
        this.messageAllowedAt = messageAllowedAt;
        this.image = image;
        this.password = password;
        this.deleted = YN.N;
        this.maxProjectGroupCount = maxProjectGroupCount;
        this.company = company;
    }
}
