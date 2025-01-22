package com.test.back.hesi.web.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

// 작업허가서 - 허가 작업내용
@Entity(name = "doc_work_permit_detail")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Where(clause = "delete_yn = 0")
public class DocWorkPermitDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doc_work_permit_id")
    private DocWorkPermit docWorkPermit;

    @Column(name = "seq")
    @Comment("순서")
    private int seq;

    @Column(name = "construction_name")
    @Comment("공사명")
    private String constructionName;

    @Column(name = "work_detail")
    @Comment("작업내용")
    private String workDetail;

    @Column(name = "worker")
    @Comment("작업팀장/반장")
    private String worker;

    @Column(name = "equipment")
    @Comment("투입 장비")
    private String equipment;

    @Column(name = "mechanical_equipment_status")
    @Comment("기계기구현황")
    private String mechanicalEquipmentStatus;
}
