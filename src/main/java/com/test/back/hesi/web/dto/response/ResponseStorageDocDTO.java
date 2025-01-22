package com.test.back.hesi.web.dto.response;

import com.querydsl.core.annotations.QueryProjection;
//import io.swagger.v3.oas.annotations.media.Schema;
import com.test.back.hesi.web.data.DocType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
//@Schema(description = "문서 저장소 목록 Response DTO")
public class ResponseStorageDocDTO {
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

    @QueryProjection
    public ResponseStorageDocDTO(Long id, String documentName, String userName, Long userId, DocType docType, LocalDateTime createdAt) {
        this.id = id;
        this.documentName = documentName;
        this.docType = docType;
        this.userName = userName;
        this.userId = userId;
        this.createdAt = createdAt.toLocalDate();
        this.realCreatedAt = createdAt;
    }
}
