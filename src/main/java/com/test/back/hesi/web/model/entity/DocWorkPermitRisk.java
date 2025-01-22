package com.test.back.hesi.web.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

// 작업허가서 - 작업 위험성 평가
@Entity(name = "doc_work_permit_risk")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Where(clause = "delete_yn = 0")
public class DocWorkPermitRisk extends BaseTimeEntity {
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

    @Column(name = "work_detail")
    @Comment("작업내용")
    private String workDetail;

    @Column(name = "risk_factor")
    @Comment("위험요인")
    private String riskFactor;

    @Column(name = "occurrence_frequency")
    @Comment("평가등급-발생빈도")
    private String occurrenceFrequency;

    @Column(name = "risk_intensity")
    @Comment("평가등급-위험강도")
    private String riskIntensity;

    @Column(name = "evaluation_result")
    @Comment("평가등급-평가결과")
    private String evaluationResult;

    @Column(name = "risk_reduction_measure")
    @Comment("위험저감대책")
    private String riskReductionMeasure;
}
