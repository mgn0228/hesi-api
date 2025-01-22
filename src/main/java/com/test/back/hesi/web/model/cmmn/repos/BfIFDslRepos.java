package com.test.back.hesi.web.model.cmmn.repos;


import com.test.back.hesi.web.model.cmmn.BfPage;

import java.util.List;

public interface BfIFDslRepos<T> {
    List<T> findAll(T obj, BfPage bfPage);

    long countAll(T obj);
}
