package com.test.back.hesi.web.repos.jpa.dsl.impl;

import com.test.back.hesi.web.model.entity.QProject;
import com.test.back.hesi.web.model.entity.QProjectGroup;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.back.hesi.web.data.ProjectAuth;
import com.test.back.hesi.web.model.cmmn.BfPage;
import com.test.back.hesi.web.model.entity.Project;
import com.test.back.hesi.web.repos.jpa.dsl.ProjectDslRepos;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProjectDslReposImpl extends QuerydslRepositorySupport implements ProjectDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public ProjectDslReposImpl() {
        super(Project.class);
    }

    private JPAQuery selectFromWhere(Project instance, QProject qProject) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qProject);

        if (!StringUtil.isNullOrEmpty(instance.getName())) {
            query.where(qProject.name.like(instance.getName()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getContents())) {
            query.where(qProject.name.like(instance.getContents()));
        }
        /*if (instance.getStatus() != null) {
            query.where(qProject.status.eq(instance.getStatus()));
        }*/
        if (instance.getId() > 0) {
            query.where(qProject.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<Project> findAll(Project instance, BfPage bfPage) {
        QProject qProject = QProject.project;
        JPAQuery query = selectFromWhere(instance, qProject);

        query
                .offset(bfPage.getOffset())
                .limit(bfPage.getPageSize())
                .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                        new PathBuilder(QProject.class, qProject.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(Project instance) {
        QProject qFile = QProject.project;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }

    @Override
    public List<Project> findUserProjectList(Long userId) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QProject qProject = QProject.project;
        QProjectGroup qProjectGroup = QProjectGroup.projectGroup;

        return jpaQueryFactory
                .select(qProject)
                .from(qProject)
                .join(qProjectGroup)
                .on(qProject.id.eq(qProjectGroup.project.id))
                .where(
                        qProject.auth.ne(ProjectAuth.ADMIN.getValue()),
                        qProjectGroup.user.id.eq(userId)
                )
                .orderBy(
                        qProject.auth.asc(),
                        qProject.createdAt.desc()
                )
                .fetch();
    }
}