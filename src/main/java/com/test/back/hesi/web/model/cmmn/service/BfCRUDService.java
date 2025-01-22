package com.test.back.hesi.web.model.cmmn.service;


import com.test.back.hesi.web.model.cmmn.BfListResponse;
import com.test.back.hesi.web.model.cmmn.BfPage;
import jakarta.servlet.http.HttpServletRequest;

public interface BfCRUDService<T> {
    T add(T obj, HttpServletRequest httpServletRequest) throws Exception;

    T edit(T obj, HttpServletRequest httpServletRequest) throws Exception;

    void remove(long seq, HttpServletRequest httpServletRequest) throws Exception;

    T find(long seq, HttpServletRequest httpServletRequest) throws Exception;

    BfListResponse<T> findAll(T obj, BfPage bfPage, HttpServletRequest httpServletRequest) throws Exception;

    T generate(T obj);
}
