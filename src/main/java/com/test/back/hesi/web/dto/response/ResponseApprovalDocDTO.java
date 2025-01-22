package com.test.back.hesi.web.dto.response;

import com.querydsl.core.annotations.QueryProjection;
//import io.swagger.v3.oas.annotations.media.Schema;
import com.test.back.hesi.web.data.DocType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
//@Schema(description = "결재 문서함 목록 Response DTO")
public class ResponseApprovalDocDTO {
    //@Schema(description = "pk")
    private Long id;

    //@Schema(description = "문서명")
    private String documentName;

    //@Schema(description = "작성자")
    private String userName;

    //@Schema(description = "작성자 pk")
    private Long userId;

    //@Schema(description = "문서 유형")
    private DocType docType;

    //@Schema(description = "생성일(작성일)")
    private LocalDate createdAt;

    //@Schema(description = "작성일")
    private LocalDateTime realCreatedAt;

    //@Schema(description = "결재완료일")
    private String approvalDate;

    //@Schema(description = "결재여부(true: 결재완료, false: 결재중 혹은 미결재)")
    private boolean isApproval;

    //@Schema(description = "포함여부")
    private boolean isInclude;

    //@Schema(description = "남은 순서 여부")
    private boolean isRemain;

    private int seq;
    private Long seqUserId;
    private String approverType;

    @QueryProjection
    public ResponseApprovalDocDTO(Long id, String documentName, String userName, Long userId, DocType docType, LocalDateTime createdAt, LocalDateTime approvalDate, boolean isApproval, boolean isInclude, boolean isRemain, int seq, Long seqUserId, String approverType) {
        this.id = id;
        this.documentName = documentName;
        this.docType = docType;
        this.userName = userName;
        this.userId = userId;
        this.createdAt = createdAt.toLocalDate();
        this.realCreatedAt = createdAt;
        if(approvalDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.approvalDate = approvalDate.format(formatter);
        } else {
            this.approvalDate = null;
        }
        this.isApproval = isApproval;
        this.isInclude = isInclude;
        this.isRemain = isRemain;
        this.seq = seq;
        this.seqUserId = seqUserId;
        this.approverType = approverType;
    }
}
