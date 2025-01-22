package com.test.back.hesi.web.repos.jpa;

import com.test.back.hesi.web.model.entity.DocWorkPermit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocWorkPermitRepos extends JpaRepository<DocWorkPermit, Long> {
    DocWorkPermit findByIdAndDeleteYn(Long id, Boolean deleteYn);
}
