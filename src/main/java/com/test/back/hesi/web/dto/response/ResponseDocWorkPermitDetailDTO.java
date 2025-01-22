package com.test.back.hesi.web.dto.response;

//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Schema(description = "작업허가서 - 허가 작업내용 Response DTO")
@Builder
@AllArgsConstructor
public class ResponseDocWorkPermitDetailDTO {
    //@Schema(description = "pk")
    private Long id;

    //@Schema(description = "순서")
    private int seq;

    //@Schema(description = "공사명")
    private String constructionName;

    //@Schema(description = "작업내용")
    private String workDetail;

    //@Schema(description = "작업팀장/반장")
    private String worker;

    //@Schema(description = "투입 장비")
    private String equipment;

    //@Schema(description = "기계기구현황")
    private String mechanicalEquipmentStatus;
}
