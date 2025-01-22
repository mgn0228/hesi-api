package com.test.back.hesi.web.dto.request;

//import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Schema(description = "작업허가서 - 작업 위험성 평가")
@AllArgsConstructor
public class RequestCreateDocWorkPermitRisk {

    //@Schema(description = "작업내용")
    @JsonProperty(value = "workDetail")
    private String workDetail;

    //@Schema(description = "위험요인")
    @JsonProperty(value = "riskFactor")
    private String riskFactor;

    //@Schema(description = "평가등급-발생빈도")
    @JsonProperty(value = "occurrenceFrequency")
    private String occurrenceFrequency;

    //@Schema(description = "평가등급-위험강도")
    @JsonProperty(value = "riskIntensity")
    private String riskIntensity;

    //@Schema(description = "평가등급-평가결과")
    @JsonProperty(value = "evaluationResult")
    private String evaluationResult;

    //@Schema(description = "위험저감대책")
    @JsonProperty(value = "riskReductionMeasure")
    private String riskReductionMeasure;
}
