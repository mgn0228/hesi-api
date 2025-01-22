package com.test.back.hesi.web.repos.jpa;

import com.test.back.hesi.web.dto.response.ResponseDocPlaceDTO;
import com.test.back.hesi.web.model.entity.DocPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocPlaceRepos extends JpaRepository<DocPlace, Long> {
    List<ResponseDocPlaceDTO> findByCompanyIdAndDeleteYnFalseOrderByPlaceAsc(Long companyId);
}
