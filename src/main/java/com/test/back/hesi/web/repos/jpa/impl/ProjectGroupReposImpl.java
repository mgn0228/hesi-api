package com.test.back.hesi.web.repos.jpa.impl;

import com.test.back.hesi.web.model.entity.QProject;
import com.test.back.hesi.web.model.entity.QProjectGroup;
import com.test.back.hesi.web.model.entity.QUsers;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.back.hesi.web.data.ProjectAuth;
import com.test.back.hesi.web.data.UserAuthType;
import com.test.back.hesi.web.dto.response.ResponseProjectMember;
import com.test.back.hesi.web.model.entity.ProjectGroup;
import com.test.back.hesi.web.repos.jpa.custom.ProjectGroupReposCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class ProjectGroupReposImpl implements ProjectGroupReposCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QProjectGroup projectGroup = QProjectGroup.projectGroup;
    final QProject project = QProject.project;
    final QUsers user = QUsers.users;

    @Transactional(readOnly = true)
    @Override
    public List<ResponseProjectMember> findByProjectId(
            Long projectId,
            String keyword,
            Boolean orderbyName,
            Boolean orderbyEmail,
            UserAuthType userType,
            Integer page,
            Integer size,
            String auth
    ){
        JPAQuery<ResponseProjectMember> query =
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        ResponseProjectMember.class,
                                        user.id.as("id"),
                                        user.userName.as("userName"),
                                        user.email.as("email"),
                                        projectGroup.userAuthType.as("userAuthType"),
                                        user.image.as("image")
                                )
                        )
                        .from(projectGroup)
                        .leftJoin(user).on(user.id.eq(projectGroup.user.id))
                        .leftJoin(project).on(project.id.eq(projectGroup.project.id))
                        .where(
                                isKeywordLike(keyword),
                                project.id.eq(projectId),
                                isUserTypeEq(userType),
                                user.useYn.eq(true)
                        );

        // 관리자 전용 프로젝트가 아니면 TEAM_USER만 보여줌
        if(!auth.equals(ProjectAuth.ADMIN.getValue())) {
            query
                    .where(
                            projectGroup.userAuthType.eq(UserAuthType.TEAM_USER)
                    );
        }

        // 페이징 정보가 있을때만 셋팅
        if(page != null || size != null){
            query
                    .offset((page - 1) * size)
                    .limit(size);
        }

        if(orderbyName != null) {
            query
                    .orderBy(
                            orderbyName ? projectGroup.user.userName.desc() : projectGroup.user.userName.asc()
                    );

        } else if(orderbyEmail != null) {
            query
                    .orderBy(
                            orderbyEmail ? projectGroup.user.email.desc() : projectGroup.user.email.asc()
                    );

        } else {
            query
                    .orderBy(
                            projectGroup.user.createdAt.desc(), projectGroup.user.userName.asc()
                    );
        }

        return query.fetch();
    }


    public List<ResponseProjectMember> findByPage(
            Long projectId, String keyword, Boolean orderbyName, Boolean orderbyEmail,
            UserAuthType userType, Integer page, Integer size, String auth)
    {
        return this.findByProjectId(projectId, keyword, orderbyName, orderbyEmail, userType, page, size, auth);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalCount(Long projectId, String keyword, UserAuthType userType, String auth){
        JPAQuery<ProjectGroup> query =
                jpaQueryFactory
                        .selectFrom(projectGroup)
                        .leftJoin(user).on(user.id.eq(projectGroup.user.id))
                        .where(
                                projectGroup.project.id.eq(projectId),
                                isKeywordLike(keyword),
                                isUserTypeEq(userType),
                                user.useYn.eq(true)
                        );

        // 관리자 전용 프로젝트가 아니면 TEAM_USER만 보여줌
        if(!auth.equals(ProjectAuth.ADMIN.getValue())) {
            query
                    .where(
                            projectGroup.userAuthType.eq(UserAuthType.TEAM_USER)
                    );
        }

        return query.fetchResults().getTotal();
    }


    private BooleanExpression isKeywordLike(String keyword) {
        return keyword != null ? user.userName.contains(keyword).or(user.email.contains(keyword)).or(user.phoneNo.contains(keyword)) : null;
    }

    private OrderSpecifier<?> orderBy(Boolean orderbyName, Boolean orderbyEmail){
        if(orderbyName != null){
            return orderbyName == true ? projectGroup.user.userName.desc() : projectGroup.user.userName.asc();
        }
        else if(orderbyEmail != null){
            return orderbyEmail == true? projectGroup.user.email.desc() : projectGroup.user.email.asc();
        }
        else{
            return projectGroup.user.userName.asc();
        }
    }

    private BooleanExpression isUserTypeEq(UserAuthType userType) {
        return userType != null ? projectGroup.userAuthType.eq(userType) : null;
    }
}
