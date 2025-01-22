package com.test.back.hesi.web.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.test.back.hesi.web.data.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 작업허가서
@Entity(name = "doc_work_permit")
@Table(indexes = { @Index(name = "template_type", columnList = "template_type") })
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class DocWorkPermit extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_name")
    @Comment("문서명")
    private String documentName;

    @OneToOne
    @JoinColumn(name = "project")
    @Comment("협력사(프로젝트)")
    private Project project;

    @OneToOne
    @JoinColumn(name = "user")
    @Comment("작성자(유저)")
    private Users user;

    @OneToOne
    @JoinColumn(name = "work_place")
    @Comment("작업장소")
    private DocPlace workPlace;

    @Column(name = "permission_period_start_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Comment("허가기간-시작")
    private LocalDate permissionPeriodStartAt;

    @Column(name = "permission_period_end_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Comment("허가기간-종료")
    private LocalDate permissionPeriodEndAt;

    @Column(name = "template_type")
    @Comment("문서 템플릿 유형")
    @Enumerated(EnumType.STRING)
    private TemplateType templateType;

    @Column(name = "approval_date")
    @Comment("허가기간-종료")
    private LocalDateTime approvalDate;

    @OneToMany(mappedBy = "docWorkPermit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("결재 정보")
    @OrderBy("seq ASC")
    private List<DocApproval> approvals = new ArrayList<>();

    @OneToMany(mappedBy = "docWorkPermit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("허가 작업내용")
    @OrderBy("seq ASC")
    private List<DocWorkPermitDetail> details = new ArrayList<>();

    @OneToMany(mappedBy = "docWorkPermit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("작업 위험성 평가")
    @OrderBy("seq ASC")
    private List<DocWorkPermitRisk> risks = new ArrayList<>();
}
