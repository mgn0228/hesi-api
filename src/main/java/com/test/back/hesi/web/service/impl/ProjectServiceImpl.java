package com.test.back.hesi.web.service.impl;

import com.test.back.hesi.utils.UserAuthUtil;
import com.test.back.hesi.web.data.ProjectAuth;
import com.test.back.hesi.web.data.UserAuthType;
import com.test.back.hesi.web.dto.request.RequestProjectDTO;
import com.test.back.hesi.web.dto.response.ResponseProjectDTO;
import com.test.back.hesi.web.model.entity.Company;
import com.test.back.hesi.web.model.entity.Project;
import com.test.back.hesi.web.model.entity.ProjectGroup;
import com.test.back.hesi.web.model.entity.Users;
import com.test.back.hesi.web.repos.jpa.CompanyRepos;
import com.test.back.hesi.web.repos.jpa.ProjectGroupRepos;
import com.test.back.hesi.web.repos.jpa.ProjectRepos;
import com.test.back.hesi.web.repos.jpa.dsl.ProjectDslRepos;
import com.test.back.hesi.web.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepos projectRepos;
    private final ProjectGroupRepos projectGroupRepos;
    private final CompanyRepos companyRepos;
    private final ProjectDslRepos projectDslRepos;
    private final UserAuthUtil userAuthUtil;

    @Override
    public Project toEntity(RequestProjectDTO requestProjectDTO) {
        return
            Project.builder()
                .name(requestProjectDTO.getName())
                .contents(requestProjectDTO.getContents())
                .auth(ProjectAuth.USER.getValue())
                .build();
    }

    @Override
    public ResponseProjectDTO toDTO(Project project) {
        return ResponseProjectDTO.builder()
            .project(project)
            .build();
    }

    @Override
    public List<ResponseProjectDTO> findByCompany(Long companyId){
        Company company = getCompany(companyId);
        return projectRepos.findByCompanyOrderByAuthAscCreatedAtDesc(company);
    }

    @Override
    public List<ResponseProjectDTO> findProjectList(Users user){
        Company company = user.getCompany();
        String projectAuth = userAuthUtil.getProjectAuth(user);

        if(projectAuth.equals(ProjectAuth.ADMIN.getValue())) {
            // ADMIN 소속이면 전체 프로젝트 리턴
            return projectRepos.findByCompanyOrderByAuthAscCreatedAtDesc(company);

        }else {
            // ADMIN 소속이 아니고 USER, SAFETY 소속이면 자신이 포함된 프로젝트만 리턴
            List<Project> list = projectDslRepos.findUserProjectList(user.getId());

            List<ResponseProjectDTO> result = new ArrayList<>();

            list.forEach(
                entity -> result.add(toDTO(entity))
            );

            return result;
        }
    }

    @Override
    public List<ResponseProjectDTO> save(RequestProjectDTO requestProjectDTO, Users user) {
        Company c = companyRepos.findById(requestProjectDTO.getCompanyId()).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "회사를 조회할 수 없습니다."));
        Optional<Project> nameCheck = projectRepos.findByNameAndCompanyAndDeleteYnFalse(requestProjectDTO.getName(), c);

        if (nameCheck.isPresent()) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "협력사명이 중복되었습니다.");
        }

        Company company = getCompany(requestProjectDTO.getCompanyId());

        Project project = this.toEntity(requestProjectDTO);
        project.setCompany(company);

        // projects INSERT
        project = projectRepos.save(project);

        // project_groups INSERT (작성자를 TEAM_MASTER로 등록)
        ProjectGroup projectGroup = ProjectGroup.builder()
            .name(project.getName())
            .userAuthType(UserAuthType.TEAM_MASTER)
            .project(project)
            .user(user)
            .build();

        projectGroupRepos.save(projectGroup);

        // Projects SELECT and return
        return this.findByCompany(requestProjectDTO.getCompanyId());
    }

    /**
     * companyId를 이용한 Company entity 조회 후 반환
     * @param companyId company table pk
     */
    private Company getCompany(Long companyId) {
        return companyRepos.findById(companyId).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "회사를 조회할 수 없습니다."));
    }
}