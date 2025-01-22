package com.test.back.hesi.web.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import jakarta.persistence.*;

// 작업 장소
@Entity(name = "doc_place")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class DocPlace extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "companyId", nullable = false)
    @Comment("회사(소속) 기본키")
    private Long companyId;

    @Column(name = "place", nullable = false)
    @Comment("장소")
    private String place;

    @Column(name = "sort", columnDefinition = "int(2)", nullable = false)
    @Comment("순서")
    private int sort;
}
