package com.test.back.hesi.web.model.entity;

import com.test.back.hesi.web.data.UserAuthType;
import lombok.*;

import jakarta.persistence.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity(name = "project_groups")
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(STRING)
    @Column(name = "user_auth_type", columnDefinition = "varchar(255) COMMENT '그룹원 권한'")
    private UserAuthType userAuthType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project", columnDefinition = "bigint COMMENT '프로젝트'")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint COMMENT '회원'")
    private Users user;

    @Column(name = "delete_yn", nullable = false)
    private boolean deleteYn;

    @Builder
    public ProjectGroup(Long id, String name, UserAuthType userAuthType, Project project, Users user) {
        super();

        this.id = id;
        this.name = name;
        this.userAuthType = userAuthType;
        this.project = project;
        this.user = user;
    }

}