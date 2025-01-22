package com.test.back.hesi.web.dto.request;

//import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.back.hesi.web.data.ProcessType;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@Schema(description = "작업허가서 생성")
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateDocWorkPermit {
    //@Schema(description = "pk, 신규: 0")
    @JsonProperty(value = "id")
    private Long id;

    //@Schema(description = "문서명")
    @JsonProperty(value = "documentName")
    private String documentName;

    //@Schema(description = "협력사(프로젝트) PK")
    @JsonProperty(value = "projectId")
    private Long projectId;

    //@Schema(description = "작업장소 PK")
    @JsonProperty(value = "docPlaceId")
    private Long docPlaceId;

    //@Schema(description = "허가기간-시작")
    @JsonProperty(value = "permissionPeriodStartAt")
    private LocalDate permissionPeriodStartAt;

    //@Schema(description = "허가기간-종료")
    @JsonProperty(value = "permissionPeriodEndAt")
    private LocalDate permissionPeriodEndAt;

    //@Schema(description = "결재 정보")
    @JsonDeserialize(contentAs = RequestCreateApproval.class)
    @JsonProperty(value = "approvals")
    private List<RequestCreateApproval> approvals;

    //@Schema(description = "허가 작업내용")
    @JsonProperty(value = "details")
    private List<RequestCreateDocWorkPermitDetail> details;

    //@Schema(description = "작업 위험성 평가")
    @JsonProperty(value = "risks")
    private List<RequestCreateDocWorkPermitRisk> risks;

    //@Schema(description = "작성 타입")
    @JsonProperty(value = "process")
    private ProcessType process;
}