package com.test.back.hesi.web.dto.request;

//import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Schema(description = "허가 작업내용")
@AllArgsConstructor
public class RequestCreateDocWorkPermitDetail {
    //@Schema(description = "공사명")
    @JsonProperty(value = "constructionName")
    private String constructionName;

    //@Schema(description = "작업내용")
    @JsonProperty(value = "workDetail")
    private String workDetail;

    //@Schema(description = "작업팀장/반장")
    @JsonProperty(value = "worker")
    private String worker;

    //@Schema(description = "투입 장비")
    @JsonProperty(value = "equipment")
    private String equipment;

    //@Schema(description = "기계기구현황")
    @JsonProperty(value = "mechanicalEquipmentStatus")
    private String mechanicalEquipmentStatus;
}
