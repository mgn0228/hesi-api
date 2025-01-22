package com.test.back.hesi.web.repos.jpa.dsl.custom;

import com.test.back.hesi.web.data.*;
import com.test.back.hesi.web.dto.request.RequestSelectDocList;
import com.test.back.hesi.web.dto.response.*;
import com.test.back.hesi.web.model.entity.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.test.back.hesi.web.model.entity.QDocApproval.docApproval;
import static com.test.back.hesi.web.model.entity.QDocWorkPermit.docWorkPermit;
import static com.test.back.hesi.web.model.entity.QProjectGroup.projectGroup;

@Repository
@RequiredArgsConstructor
public class DocReposCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<ResponseApprovalDocDTO> getApprovalDocList(Pageable pageable, Long projectId, Long companyId, Long userId, RequestSelectDocList requestSelectDocList) {
        List<ResponseApprovalDocDTO> allDocs = new ArrayList<>();

        // 작업허가서 서브쿼리1 (내 협력사의 직원이 결재자에 포함된 게시물 목록)
        List<Long> subQuery1_1 = jpaQueryFactory
            .select(docWorkPermit.id)
            .from(docWorkPermit)
            .join(docApproval).on(docWorkPermit.id.eq(docApproval.docWorkPermit.id))
            .join(projectGroup).on(projectGroup.user.id.eq(docApproval.user.id))
            .where(
                docWorkPermit.templateType.eq(TemplateType.APPROVAL_DOCUMENT)
                , docWorkPermit.deleteYn.eq(false)
                , (projectId == null ? null : projectGroup.userAuthType.ne(UserAuthType.TEAM_MASTER))
                , (projectId == null ? null : projectGroup.project.id.eq(projectId))
                , eqCompanyId(companyId, "docWorkPermit")
                , likeKeyword(requestSelectDocList.getKeyword(), "docWorkPermit")
                , isOnlyMe(userId, "docWorkPermit", requestSelectDocList.getIsOnlyMe())
            )
            .groupBy(docWorkPermit.id).fetch();

        // 작업허가서 서브쿼리2 (내 협력사의 직원이 작성한 게시물 목록)
        List<Long> subQuery1_2 = jpaQueryFactory
            .select(docWorkPermit.id)
            .from(docApproval)
            .leftJoin(docWorkPermit).on(docWorkPermit.id.eq(docApproval.docWorkPermit.id))
            .where(
                docWorkPermit.templateType.eq(TemplateType.APPROVAL_DOCUMENT)
                , docWorkPermit.deleteYn.eq(false)
                , (projectId == null ? null : docWorkPermit.project.id.eq(projectId))
                , eqCompanyId(companyId, "docWorkPermit")
                , likeKeyword(requestSelectDocList.getKeyword(), "docWorkPermit")
                , isOnlyMe(userId, "docWorkPermit", requestSelectDocList.getIsOnlyMe())
            )
            .groupBy(docWorkPermit.id).fetch();

        // 작업허가서 메인쿼리
        JPAQuery<ResponseApprovalDocDTO> query1 = jpaQueryFactory
            .select(Projections.constructor(
                ResponseApprovalDocDTO.class
                , docWorkPermit.id
                , docWorkPermit.documentName
                , docWorkPermit.user.userName
                , docWorkPermit.user.id
                , Expressions.constant(DocType.DOC_WORK_PERMIT)
                , docWorkPermit.createdAt
                , docWorkPermit.approvalDate
                , docWorkPermit.approvalDate.isNotNull()
                , Expressions.cases()
                    .when(JPAExpressions.select(docApproval.count())
                        .from(docApproval)
                        .where(
                            docApproval.docWorkPermit.id.eq(docWorkPermit.id)
                            , docApproval.user.id.in(userId)
                        ).gt(0L)).then(true)
                    .otherwise(false)
                , Expressions.cases()
                    .when(JPAExpressions.select(docApproval.count())
                        .from(docApproval)
                        .where(
                            docApproval.docWorkPermit.id.eq(docWorkPermit.id)
                            , docApproval.user.id.in(userId)
                            , docApproval.approvalDate.isNull()
                        ).gt(0L)).then(true)
                    .otherwise(false)
                , JPAExpressions.select(docApproval.seq.min())
                    .from(docApproval)
                    .where(
                        docApproval.docWorkPermit.id.eq(docWorkPermit.id)
                        , docApproval.approvalDate.isNull()
                    ).groupBy(docApproval.docWorkPermit.id)
                , JPAExpressions.select(docApproval.user.id)
                    .from(docApproval)
                    .where(
                        docApproval.docWorkPermit.id.eq(docWorkPermit.id)
                        , docApproval.approvalDate.isNull()
                        , docApproval.seq.eq(
                            JPAExpressions.select(docApproval.seq.min())
                                .from(docApproval)
                                .where(
                                    docApproval.docWorkPermit.id.eq(docWorkPermit.id)
                                    , docApproval.approvalDate.isNull()
                                ).groupBy(docApproval.docWorkPermit.id)
                        )
                    ).groupBy(docApproval.docWorkPermit.id)
                , JPAExpressions.select(docApproval.approverType)
                    .from(docApproval)
                    .where(
                        docApproval.docWorkPermit.id.eq(docWorkPermit.id)
                        , docApproval.approvalDate.isNull()
                        , docApproval.seq.eq(
                            JPAExpressions.select(docApproval.seq.min())
                                .from(docApproval)
                                .where(
                                    docApproval.docWorkPermit.id.eq(docWorkPermit.id)
                                    , docApproval.approvalDate.isNull()
                                ).groupBy(docApproval.docWorkPermit.id)
                        )
                    ).groupBy(docApproval.docWorkPermit.id)
            ))
            .from(docWorkPermit)
            .where(
                docWorkPermit.deleteYn.eq(false),
                docWorkPermit.id.in(subQuery1_1).or(docWorkPermit.id.in(subQuery1_2))
            )
            .groupBy(docWorkPermit.id);

        List<ResponseApprovalDocDTO> docs01 = query1.fetchResults().getResults();

        allDocs.addAll(docs01);

        // 데이터가 없으면 빈 페이지 반환
        if (allDocs.isEmpty()) {
            return Page.empty(pageable);
        }
        if (requestSelectDocList.getIsAsc()) {
            if (requestSelectDocList.getDocSortType() == DocSortType.DOCUMENT_NAME) {
                allDocs.sort(Comparator.comparing(ResponseApprovalDocDTO::getDocumentName));
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.USER_NAME) {
                allDocs.sort(Comparator.comparing(ResponseApprovalDocDTO::getUserName));
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.DOCUMENT_STATUS) {
                List<ResponseApprovalDocDTO> newAllDocs = new ArrayList<>();

                // 결재하기
                newAllDocs.addAll(allDocs.stream().filter(dto ->
                    !dto.isApproval() && dto.isInclude() && dto.isRemain() && Objects.equals(dto.getSeqUserId(), userId)
                ).sorted(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed()).collect(Collectors.toList()));
                // 결재대기
                newAllDocs.addAll(allDocs.stream().filter(dto ->
                    !dto.isApproval() && dto.isInclude() && dto.isRemain() && !Objects.equals(dto.getSeqUserId(), userId)
                ).sorted(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed()).collect(Collectors.toList()));
                // 결재중
                newAllDocs.addAll(allDocs.stream().filter(dto ->
                    !dto.isApproval() && dto.isInclude() && !dto.isRemain()
                ).sorted(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed()).collect(Collectors.toList()));
                // 결과보기
                newAllDocs.addAll(allDocs.stream().filter(
                    dto -> !dto.isInclude() || dto.isApproval()
                ).sorted(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed()).collect(Collectors.toList()));

                allDocs = newAllDocs;
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.CREATE_AT) {
                allDocs.sort(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt));
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.APPROVAL_DATE) {
                allDocs.sort(Comparator.nullsLast(
                    Comparator.comparing(ResponseApprovalDocDTO::getApprovalDate, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(ResponseApprovalDocDTO::getRealCreatedAt, Comparator.reverseOrder())
                ));
            }
        } else {
            if (requestSelectDocList.getDocSortType() == DocSortType.DOCUMENT_NAME) {
                allDocs.sort(Comparator.comparing(ResponseApprovalDocDTO::getDocumentName).reversed());
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.USER_NAME) {
                allDocs.sort(Comparator.comparing(ResponseApprovalDocDTO::getUserName).reversed());
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.DOCUMENT_STATUS) {
                List<ResponseApprovalDocDTO> newAllDocs = new ArrayList<>();

                // 결재하기
                newAllDocs.addAll(allDocs.stream().filter(dto ->
                    !dto.isApproval() && dto.isInclude() && dto.isRemain() && Objects.equals(dto.getSeqUserId(), userId)
                ).sorted(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed()).collect(Collectors.toList()));
                // 결재대기
                newAllDocs.addAll(allDocs.stream().filter(dto ->
                    !dto.isApproval() && dto.isInclude() && dto.isRemain() && !Objects.equals(dto.getSeqUserId(), userId)
                ).sorted(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed()).collect(Collectors.toList()));
                // 결재중
                newAllDocs.addAll(allDocs.stream().filter(dto ->
                    !dto.isApproval() && dto.isInclude() && !dto.isRemain()
                ).sorted(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed()).collect(Collectors.toList()));
                // 결과보기
                newAllDocs.addAll(allDocs.stream().filter(
                    dto -> !dto.isInclude() || dto.isApproval()
                ).sorted(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed()).collect(Collectors.toList()));

                /* 역정렬 */
                Collections.reverse(newAllDocs);

                allDocs = newAllDocs;
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.CREATE_AT) {
                allDocs.sort(Comparator.comparing(ResponseApprovalDocDTO::getRealCreatedAt).reversed());
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.APPROVAL_DATE) {
                allDocs.sort(Comparator.nullsLast(
                    Comparator.comparing(ResponseApprovalDocDTO::getApprovalDate, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(ResponseApprovalDocDTO::getRealCreatedAt, Comparator.reverseOrder())
                ).reversed());
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allDocs.size());

        if (start >= allDocs.size() || start >= end) {
            return Page.empty(pageable);
        }

        List<ResponseApprovalDocDTO> sublist = allDocs.subList(start, end);
        return new PageImpl<>(sublist, pageable, allDocs.size());
    }

    public Page<ResponseStorageDocDTO> getStorageDocList(Pageable pageable, Long projectId, Long companyId, RequestSelectDocList requestSelectDocList) {
        List<ResponseStorageDocDTO> allDocs = new ArrayList<>();

        DocType docType = requestSelectDocList.getDocType();

        List<ResponseStorageDocDTO> docs01 = docType == DocType.ALL || docType == DocType.DOC_WORK_PERMIT ? jpaQueryFactory
            .select(Projections.constructor(
                ResponseStorageDocDTO.class
                , docWorkPermit.id
                , docWorkPermit.documentName
                , docWorkPermit.user.userName
                , docWorkPermit.user.id
                , Expressions.constant(DocType.DOC_WORK_PERMIT)
                , docWorkPermit.createdAt
            ))
            .from(docWorkPermit)
            .where(
                eqProjectId(projectId, "docWorkPermit")
                , eqCompanyId(companyId, "docWorkPermit")
                , docWorkPermit.templateType.eq(TemplateType.DOCUMENT_STORAGE)
                , docWorkPermit.deleteYn.eq(false)
                , likeKeyword(requestSelectDocList.getKeyword(), "docWorkPermit")
            )
            .fetchResults()
            .getResults() : new ArrayList<>();

        allDocs.addAll(docs01);

        // 데이터가 없으면 빈 페이지 반환
        if (allDocs.isEmpty()) {
            return Page.empty(pageable);
        }
        if (requestSelectDocList.getIsAsc()) {
            if (requestSelectDocList.getDocSortType() == DocSortType.CREATE_AT) {
                allDocs.sort(Comparator.comparing(ResponseStorageDocDTO::getRealCreatedAt));
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.DOCUMENT_NAME) {
                allDocs.sort(Comparator.comparing(ResponseStorageDocDTO::getDocumentName));
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.USER_NAME) {
                allDocs.sort(Comparator.comparing(ResponseStorageDocDTO::getUserName));
            }
        } else {
            if (requestSelectDocList.getDocSortType() == DocSortType.CREATE_AT) {
                allDocs.sort(Comparator.comparing(ResponseStorageDocDTO::getRealCreatedAt).reversed());
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.DOCUMENT_NAME) {
                allDocs.sort(Comparator.comparing(ResponseStorageDocDTO::getDocumentName).reversed());
            }
            if (requestSelectDocList.getDocSortType() == DocSortType.USER_NAME) {
                allDocs.sort(Comparator.comparing(ResponseStorageDocDTO::getUserName).reversed());
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allDocs.size());

        if (start >= allDocs.size() || start >= end) {
            return Page.empty(pageable);
        }

        List<ResponseStorageDocDTO> sublist = allDocs.subList(start, end);
        return new PageImpl<>(sublist, pageable, allDocs.size());
    }

    public Page<ResponseDocWorkPermitDTO> getDocWorkPermitList(Pageable pageable, Long projectId, Long companyId, RequestSelectDocList requestSelectDocList) {
        JPAQuery<ResponseDocWorkPermitDTO> query = jpaQueryFactory
            .select(Projections.constructor(
                ResponseDocWorkPermitDTO.class
                , docWorkPermit.id
                , docWorkPermit.documentName
                , docWorkPermit.user.userName
                , docWorkPermit.createdAt
            ))
            .from(docWorkPermit)
            .where(
                eqProjectId(projectId, "docWorkPermit")
                , eqCompanyId(companyId, "docWorkPermit")
                , docWorkPermit.templateType.eq(TemplateType.DOCUMENT_CREATED)
                , docWorkPermit.deleteYn.eq(false)
                , likeKeyword(requestSelectDocList.getKeyword(), "docWorkPermit")
            );

        if(!Objects.isNull(requestSelectDocList.getDocSortType())) {
            query.orderBy(setDocOrderBy(requestSelectDocList, "docWorkPermit"));

        }else {
            query.orderBy(docWorkPermit.createdAt.desc());
        }

        QueryResults<ResponseDocWorkPermitDTO> results = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    public DocWorkPermit getDocWorkPermitDetail(Long id, Long projectId, Long companyId) {
        return jpaQueryFactory
            .select(docWorkPermit)
            .from(docWorkPermit)
            .where(
                docWorkPermit.id.eq(id)
                , eqProjectId(projectId, "docWorkPermit")
                , eqCompanyId(companyId, "docWorkPermit")
                , docWorkPermit.deleteYn.eq(false)
            )
            .fetchOne();
    }

    public Integer checkApprovalDetailAuth01(Long projectId, Long docId, String docType) {
        /*
        * 로직 해석
        * 게시물 원본 메인 테이블에서 작성자 id를 조회
        * 내 회사 그룹원 중에 해당 id가 있는지 조회
        * */

        Long userId;

        switch (docType) {
            case "doc-work-permit" :
                userId = jpaQueryFactory
                    .select(docWorkPermit.user.id)
                    .from(docWorkPermit)
                    .where(
                        docWorkPermit.id.eq(docId)
                        ,docWorkPermit.deleteYn.eq(false)
                    )
                    .fetchOne();
                break;
            default: userId = 0L;
        }

        return jpaQueryFactory
            .selectOne()
            .from(projectGroup)
            .where(
                projectGroup.project.id.eq(projectId)
                ,projectGroup.user.id.eq(userId)
                ,projectGroup.deleteYn.eq(false)
                ,projectGroup.userAuthType.ne(UserAuthType.TEAM_MASTER)
            )
            .fetchOne();
    }

    public Integer checkApprovalDetailAuth02(Long projectId, Long docId, String docType) {
        /*
         * 로직 해석
         * 내 회사 그룹원들의 id 리스트를 조회
         * 결재자 중에서 내 회사 그룹원들이 있는지 조회
         * */

        List<Long> userId = jpaQueryFactory
            .select(projectGroup.user.id)
            .from(projectGroup)
            .where(
                projectGroup.project.id.eq(projectId)
                ,projectGroup.deleteYn.eq(false)
                ,projectGroup.userAuthType.ne(UserAuthType.TEAM_MASTER)
            )
            .fetch();

        switch (docType) {
            case "doc-work-permit" :
                return jpaQueryFactory
                    .selectOne()
                    .from(docApproval)
                    .where(
                        docApproval.docWorkPermit.id.eq(docId)
                        ,docApproval.user.id.in(userId)
                        ,docApproval.deleteYn.eq(false)
                    )
                    .fetchOne();
            default: return 0;
        }
    }

    private BooleanExpression eqProjectId(Long projectId, String type) {
        if (projectId == null) {
            return null;

        } else {
            if (type.equals("docWorkPermit")) {
                return docWorkPermit.project.id.eq(projectId);
            } else {
                return null;
            }
        }
    }

    private BooleanExpression eqCompanyId(Long companyId, String type) {
        if (companyId == null) {
            return null;

        } else {
            if (type.equals("docWorkPermit")) {
                return docWorkPermit.project.company.id.eq(companyId);
            }else {
                return null;
            }
        }
    }

    private BooleanExpression isOnlyMe(Long userId, String type, Boolean isOnlyMe) {
        if (!isOnlyMe || userId == null) {
            return null;
        } else {
            if (type.equals("docWorkPermit")) {
                return docWorkPermit.user.id.eq(userId).or(docApproval.user.id.eq(userId));
            } else {
                return null;
            }
        }
    }

    private BooleanExpression likeKeyword(String keyword, String type) {
        if (!StringUtils.isEmpty(keyword) && !keyword.equals("")) {
            if (type.equals("docWorkPermit")) {
                return docWorkPermit.documentName.contains(keyword);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private OrderSpecifier<?> setDocOrderBy(RequestSelectDocList requestSelectDocList, String docType) {
        if (requestSelectDocList.getIsAsc()) {
            switch (docType) {
                case "docWorkPermit" :
                    if (requestSelectDocList.getDocSortType() == DocSortType.CREATE_AT) {
                        return docWorkPermit.createdAt.asc();
                    }else {
                        return docWorkPermit.documentName.asc();
                    }
                default : return null;
            }

        } else {
            switch (docType) {
                case "docWorkPermit" :
                    if (requestSelectDocList.getDocSortType() == DocSortType.CREATE_AT) {
                        return docWorkPermit.createdAt.desc();
                    }else {
                        return docWorkPermit.documentName.desc();
                    }
                default : return null;
            }
        }
    }
}