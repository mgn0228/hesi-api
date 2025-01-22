package com.test.back.hesi.web.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Schema
public class RequestProjectDTO {
    //@Schema
    long userId;

    //@Schema
    String name;

    //@Schema
    String contents;

    //@Schema
    long companyId;
}