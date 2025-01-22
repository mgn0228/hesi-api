package com.test.back.hesi.web.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import jakarta.persistence.*;

@Entity(name = "company")
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("기본키")
    private Long id;

    @Column(name = "name")
    @Comment("회사명")
    private String name;

    public Company(Company company) {
        this.id = company.id;
        this.name = company.name;
    }

    @Builder
    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}