package com.test.back.hesi.web.repos.jpa.dsl;

import com.test.back.hesi.web.model.cmmn.repos.BfIFDslRepos;
import com.test.back.hesi.web.model.entity.Project;

import java.util.List;

public interface ProjectDslRepos extends BfIFDslRepos<Project> {
    List<Project> findUserProjectList(Long userId);
}