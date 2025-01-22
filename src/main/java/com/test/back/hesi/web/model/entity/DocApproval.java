package com.test.back.hesi.web.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 결재 정보
@Entity(name = "doc_approval")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class DocApproval extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doc_work_permit_id")
    @Comment("작업허가서")
    private DocWorkPermit docWorkPermit;
/*
    @ManyToOne
    @JoinColumn(name = "doc_time_work_permit_id")
    @Comment("작업허가서(야간/조출/주말)")
    private DocTimeWorkPermit docTimeWorkPermit;

    @ManyToOne
    @JoinColumn(name = "doc_object_handling_plan_id")
    @Comment("중량물 취급 계획서")
    private DocObjectHandlingPlan docObjectHandlingPlan;

    @ManyToOne
    @JoinColumn(name = "doc_space_work_permit_id")
    @Comment("밀폐공간 작업허가서")
    private DocSpaceWorkPermit docSpaceWorkPermit;

    @ManyToOne
    @JoinColumn(name = "doc_risk_assessment_table_id")
    @Comment("위험성 평가표")
    private DocRiskAssessmentTable docRiskAssessmentTable;

    @ManyToOne
    @JoinColumn(name = "doc_transport_handling_plan_id")
    @Comment("지게차 / 트레일러 중량물 취급 계획서")
    private DocTransportHandlingPlan docTransportHandlingPlan;
*/
    @Column(name = "approver_type")
    @Comment("결재자 유형")
    private String approverType;

    @Column(name = "seq")
    @Comment("순서")
    private int seq;

    @OneToOne
    @JoinColumn(name = "user")
    @Comment("결재자(유저)")
    private Users user;

    @Column(name = "signature")
    @Comment("서명")
    private String signature;

    @Column(name = "opinion")
    @Comment("의견")
    private String opinion = "";

    @Column(name = "approval_date")
    @Comment("결재완료일")
    private LocalDateTime approvalDate;

    public void update(DocApproval project) {
        this.user = project.getUser();
        this.approverType = project.getApproverType();
        this.signature = project.getSignature();
        this.opinion = project.getOpinion();
        this.approvalDate = project.getApprovalDate();
    }
}
