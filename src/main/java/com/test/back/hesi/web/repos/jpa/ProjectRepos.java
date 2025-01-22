package com.test.back.hesi.web.repos.jpa;

import com.test.back.hesi.web.dto.response.ResponseProjectDTO;
import com.test.back.hesi.web.model.entity.Company;
import com.test.back.hesi.web.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepos extends JpaRepository<Project, Long>/*, ProjectRepositoryCustom*/ {
    List<ResponseProjectDTO> findByCompanyOrderByAuthAscCreatedAtDesc(Company company);
    Optional<Project> findById(Long id);
    List<Project> findAllByDeleteYnAndCompany(Boolean deleteYn, Company company);
    List<Project> findAllByDeleteYnAndAuthAndCompany(Boolean deleteYn, String auth, Company company);
    Optional<Project> findByIdAndDeleteYnFalse(Long id);
    Optional<Project> findByNameAndCompanyAndDeleteYnFalse(String name, Company company);
}