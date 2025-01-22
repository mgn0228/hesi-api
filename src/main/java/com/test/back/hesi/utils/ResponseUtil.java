package com.test.back.hesi.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static ResponseEntity sendResponse(Object obj) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("result", true);
        body.put("msg", null);
        body.put("data", obj);

        return ResponseEntity.ok()
            .headers(headers)
            .body(body);
    }

    public static ResponseEntity sendResponse(HttpStatus httpStatus, String cause) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("result", false);
        body.put("msg", cause);

        return ResponseEntity.status(httpStatus).headers(headers).body(body);
    }
}
