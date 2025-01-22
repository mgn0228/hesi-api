package com.test.back.hesi.web.model.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "projects")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "contents")
    private String contents;

    @Column(name = "auth")
    private String auth;

    @ManyToOne
    @JoinColumn(name = "company", columnDefinition = "bigint COMMENT '회사(소속) 기본키'")
    private Company company;

    @Builder
    public Project(long id, String name, String contents, String auth, Company company, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.auth = auth;
        this.contents = contents;
        this.company = company;
    }

    public void update(Project project){
        this.name = project.getName();
        this.contents = project.getContents();
    }
}
