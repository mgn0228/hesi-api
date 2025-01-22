package com.test.back.hesi.web.repos.jpa;

import com.test.back.hesi.web.data.UserAuthType;
import com.test.back.hesi.web.model.entity.Project;
import com.test.back.hesi.web.model.entity.ProjectGroup;
import com.test.back.hesi.web.model.entity.Users;
import com.test.back.hesi.web.repos.jpa.custom.ProjectGroupReposCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectGroupRepos extends JpaRepository<ProjectGroup, Long>, ProjectGroupReposCustom {
    Long countByProjectAndUser(Project project, Users user);
    List<ProjectGroup> findByUserAndDeleteYn(Users user, boolean deleteYn);
    List<ProjectGroup> findAllByProject(Project project);
    ProjectGroup findByUserAndProject(Users user, Project project);
    List<ProjectGroup> findAllByUserAndUserAuthType(Users user, UserAuthType type);
    List<ProjectGroup> findAllByProjectAndUserAuthType(Project project, UserAuthType type);
    List<ProjectGroup> findAllByProjectAndDeleteYn(Project project, boolean deleteYn);
}