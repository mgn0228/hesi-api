package com.test.back.hesi.web.dto.response;

import com.querydsl.core.annotations.QueryProjection;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Schema(description = "문서의 작업 장소")
@Builder
public class ResponseDocPlaceDTO {
    //@Schema(description = "pk")
    private Long id;

    //@Schema(description = "장소")
    private String place;

    @QueryProjection
    public ResponseDocPlaceDTO(Long id, String place) {
        this.id = id;
        this.place = place;
    }
}