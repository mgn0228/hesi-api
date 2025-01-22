package com.test.back.hesi.web.service;

import com.test.back.hesi.web.dto.request.RequestProjectDTO;
import com.test.back.hesi.web.dto.response.ResponseProjectDTO;
import com.test.back.hesi.web.model.entity.Project;
import com.test.back.hesi.web.model.entity.Users;

import java.util.List;

public interface ProjectService {
    /**
     * RequestProjectDTO를  Project entity로 복사 후 반환
     * @param requestProjectDTO request project DTO
     */
    Project toEntity(RequestProjectDTO requestProjectDTO);

    /**
     * Project entity를  RequestProjectDTO로 복사 후 반환
     * @param project Project entity
     */
    ResponseProjectDTO toDTO(Project project);

    /**
     * companyId를 이용한 소속 회사의 모든 프로젝트 조회 후 반환
     * @param companyId company table pk
     */
    List<ResponseProjectDTO> findByCompany(Long companyId) throws Exception;

    /**
     * companyId를 이용한 소속 회사의 모든 프로젝트 조회 후 반환
     * @param user Users entity
     */
    List<ResponseProjectDTO> findProjectList(Users user) throws Exception;

    /**
     * RequestProjectDTO를 이용한 프로젝트 저장 및 현재 프로젝트 전체 리스트 반환
     * @param requestProjectDTO request project DTO
     */
    List<ResponseProjectDTO> save(RequestProjectDTO requestProjectDTO, Users user) throws Exception;
}