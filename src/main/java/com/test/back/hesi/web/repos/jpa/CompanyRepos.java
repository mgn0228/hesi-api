package com.test.back.hesi.web.repos.jpa;

import com.test.back.hesi.web.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepos extends JpaRepository<Company, Long> {
}