package com.test.back.hesi.web.dto.response;

import com.querydsl.core.annotations.QueryProjection;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@Schema(description = "작업허가서 Response DTO")
@Builder
@AllArgsConstructor
public class ResponseDocWorkPermitDTO {
    //@Schema(description = "pk")
    private Long id;

    //@Schema(description = "문서명")
    private String documentName;

    //@Schema(description = "작성자 pk")
    private Long userId;

    //@Schema(description = "작성자 이름")
    private String userName;

    //@Schema(description = "작성자 프로필 이미지")
    private String userImg;

    //@Schema(description = "프로젝트 pk(업체)")
    private Long projectId;

    //@Schema(description = "프로젝트 이름(업체명)")
    private String projectName;

    //@Schema(description = "작업장소")
    private ResponseDocPlaceDTO workPlace;

    //@Schema(description = "허가기간-시작")
    private LocalDate permissionPeriodStartAt;

    //@Schema(description = "허가기간-종료")
    private LocalDate permissionPeriodEndAt;

    //@Schema(description = "결재 정보")
    private List<ResponseDocApprovalDTO> approvals = new ArrayList<>();

    //@Schema(description = "허가 작업내용")
    private List<ResponseDocWorkPermitDetailDTO> details = new ArrayList<>();

    //@Schema(description = "작업 위험성 평가")
    private List<ResponseDocWorkPermitRiskDTO> risks = new ArrayList<>();

    //@Schema(description = "작성일")
    private LocalDateTime createdAt;

    @QueryProjection
    public ResponseDocWorkPermitDTO(Long id, String documentName, String userName, LocalDateTime createdAt) {
        this.id = id;
        this.documentName = documentName;
        this.userName = userName;
        this.createdAt = createdAt;
    }
}
