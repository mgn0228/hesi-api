package com.test.back.hesi.web.dto.response;

//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Schema(description = "작업허가서 - 작업 위험성 평가 Response DTO")
@Builder
@AllArgsConstructor
public class ResponseDocWorkPermitRiskDTO {
    //@Schema(description = "pk")
    private Long id;

    //@Schema(description = "순서")
    private int seq;

    //@Schema(description = "작업내용")
    private String workDetail;

    //@Schema(description = "위험요인")
    private String riskFactor;

    //@Schema(description = "평가등급-발생빈도")
    private String occurrenceFrequency;

    //@Schema(description = "평가등급-위험강도")
    private String riskIntensity;

    //@Schema(description = "평가등급-평가결과")
    private String evaluationResult;

    //@Schema(description = "위험저감대책")
    private String riskReductionMeasure;
}
