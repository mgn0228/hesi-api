package com.test.back.hesi.web.dto.request;

//import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@Schema(description = "결재 정보")
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateApproval {
    //@Schema(description = "pk")
    @JsonProperty(value = "id")
    private Long id;

    //@Schema(description = "결재자 유형")
    @JsonProperty(value = "approverType")
    private String approverType;

    //@Schema(description = "결재자 pk")
    @JsonProperty(value = "userId")
    private Long userId;

    //@Schema(description = "서명")
    @JsonProperty(value = "signature")
    private String signature;

    //@Schema(description = "의견")
    @JsonProperty(value = "opinion")
    private String opinion;
}
