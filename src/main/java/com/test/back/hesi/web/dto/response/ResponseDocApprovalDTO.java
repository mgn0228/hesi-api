package com.test.back.hesi.web.dto.response;

//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
//@Schema(description = "결재 Response DTO")
@Builder
public class ResponseDocApprovalDTO {
    //@Schema(description = "pk")
    private Long id;

    //@Schema(description = "결재자 유형")
    private String approverType;

    //@Schema(description = "결재자명")
    private String approverName;

    //@Schema(description = "결재자 pk")
    private Long approverId;

    //@Schema(description = "서명")
    private String signature;

    //@Schema(description = "의견")
    private String opinion;

    //@Schema(description = "순서")
    private int seq;

    //@Schema(description = "결재완료일")
    private LocalDateTime approvalDate;
}
