package com.test.back.hesi.web.data;

// 문서 정렬 유형
public enum DocSortType {

    // 생성일(작성일)
    CREATE_AT,

    // 문서 이름
    DOCUMENT_NAME,

    // 작성자
    USER_NAME,

    // 문서상태,
    DOCUMENT_STATUS,

    // 결재완료일,
    APPROVAL_DATE
}