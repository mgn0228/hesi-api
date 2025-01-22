package com.test.back.hesi.web.dto.request;

//import io.swagger.v3.oas.annotations.media.Schema;
import com.test.back.hesi.web.data.DocSortType;
import com.test.back.hesi.web.data.DocType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Schema(description = "문서 목록 조회 DTO")
@AllArgsConstructor
public class RequestSelectDocList {
    //@Schema(description = "오름차순 여부")
    private Boolean isAsc;

    //@Schema(description = "정렬 유형")
    private DocSortType docSortType;

    //@Schema(description = "문서 종류")
    private DocType docType;

    //@Schema(description = "검색어")
    private String keyword;

    //@Schema(description = "나만 보기")
    private Boolean isOnlyMe;
}