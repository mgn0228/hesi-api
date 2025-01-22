package com.test.back.hesi.web.repos.direct;

import com.test.back.hesi.web.data.DocSortType;
import com.test.back.hesi.web.data.ProjectAuth;
import com.test.back.hesi.web.dto.request.RequestSelectDocList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class DirectQuery {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectQuery(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> findUserProjectAuth(Long userId) {
        try {
            Map<String, Object> userProjectAuth = jdbcTemplate.queryForMap(
                    "SELECT " +
                            "COUNT(case when p.auth = 'ADMIN' then 1 END) AS adminCnt " +
                            ",COUNT(case when p.auth = 'SAFETY' then 1 END) AS safetyCnt " +
                            "FROM project_groups pg " +
                            "JOIN projects p " +
                            "ON pg.project = p.id " +
                            "WHERE pg.user = " + userId + " " +
                            "AND pg.delete_yn = 0 " +
                            "AND p.delete_yn = 0"
            );
            return userProjectAuth;
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getApprovalDocList(
            int page, int size, String auth, Long companyId, Long projectId, Long userId,
            RequestSelectDocList requestSelectDocList) {
        // 결재 문서함 목록 조회 로직
        String orderBy;
        String where01 = "";
        String where02 = "";
        String where03 = "";

        // ORDER BY
        if(requestSelectDocList.getIsAsc()) { // 오름차순
            if(requestSelectDocList.getDocSortType() == DocSortType.DOCUMENT_NAME) { // 문서 이름
                orderBy = " ORDER BY d.document_name ";
            }else if(requestSelectDocList.getDocSortType() == DocSortType.USER_NAME) { // 작성자
                orderBy = " ORDER BY u.user_name, d.created_at DESC ";
            }else if(requestSelectDocList.getDocSortType() == DocSortType.CREATE_AT) { // 작성일
                orderBy = " ORDER BY d.created_at ";
            }else if(requestSelectDocList.getDocSortType() == DocSortType.APPROVAL_DATE) { // 결재완료일
                orderBy = " ORDER BY d.approval_date ";
            }else { // 문서 상태
                orderBy = " ORDER BY (CASE WHEN d.approver_user = " + userId + " THEN 0 ELSE 1 END), d.created_at DESC ";
            }
        }else { // 내림차순
            if(requestSelectDocList.getDocSortType() == DocSortType.DOCUMENT_NAME) {
                orderBy = " ORDER BY d.document_name DESC ";
            }else if(requestSelectDocList.getDocSortType() == DocSortType.USER_NAME) {
                orderBy = " ORDER BY u.user_name DESC, d.created_at DESC ";
            }else if(requestSelectDocList.getDocSortType() == DocSortType.CREATE_AT) {
                orderBy = " ORDER BY d.created_at DESC ";
            }else if(requestSelectDocList.getDocSortType() == DocSortType.APPROVAL_DATE) {
                orderBy = " ORDER BY d.approval_date DESC ";
            }else {
                orderBy = " ORDER BY (CASE WHEN d.approver_user = " + userId + " THEN 1 ELSE 0 END), d.created_at DESC ";
            }
        }

        // ADMIN(관리자)가 아닌 경우 조건 추가
        if(!ProjectAuth.ADMIN.getValue().equals(auth)) {
            where01 = " 	AND (d.project = " + projectId + " OR pg.`user` IS NOT NULL) ";
        }

        // 내 문서만 보기
        if(requestSelectDocList.getIsOnlyMe()) {
            where02 = " 	AND d.project = " + projectId + " ";
        }

        // 검색어 입력
        if(StringUtils.isNotEmpty(requestSelectDocList.getKeyword()) && StringUtils.isNotBlank(requestSelectDocList.getKeyword())) {
            where03 = " 	AND d.document_name LIKE '%" + requestSelectDocList.getKeyword() + "%' ";
        }

        // 전체 개수 조회
        String countQuery = "" +
                " SELECT SUM(d.cnt) AS totalCnt " +
                " FROM ( " +

                " 	SELECT COUNT(DISTINCT d.id) AS cnt" +
                " 	FROM doc_work_permit d " +
                " 	JOIN doc_approval a ON d.id = a.doc_work_permit_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " 	UNION ALL " +

                " 	SELECT COUNT(DISTINCT d.id) AS cnt" +
                " 	FROM doc_time_work_permit d " +
                " 	JOIN doc_approval a ON d.id = a.doc_time_work_permit_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " 	UNION ALL " +

                " 	SELECT COUNT(DISTINCT d.id) AS cnt" +
                " 	FROM doc_object_handling_plan d " +
                " 	JOIN doc_approval a ON d.id = a.doc_object_handling_plan_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " 	UNION ALL " +

                " 	SELECT COUNT(DISTINCT d.id) AS cnt" +
                " 	FROM doc_space_work_permit d " +
                " 	JOIN doc_approval a ON d.id = a.doc_space_work_permit_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " 	UNION ALL " +

                " 	SELECT COUNT(DISTINCT d.id) AS cnt" +
                " 	FROM doc_risk_assessment_table d " +
                " 	JOIN doc_approval a ON d.id = a.doc_risk_assessment_table_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " ) d ";

        // 목록 조회
        String listQuery = "" +
                " SELECT " +
                " 	d.id " +
                " 	,d.user " +
                " 	,d.document_name AS documentName " +
                " 	,SUBSTRING(d.created_at,1,10) AS createdAt " +
                " 	,SUBSTRING(d.approval_date,1,16) AS approvalDate " +
                " 	,d.seq " +
                " 	,d.approver_type AS approverType " +
                " 	,d.approver_user AS approverUser " +
                " 	,u.user_name AS userName " +
                " 	,d.docType " +
                " FROM ( " +

                " 	SELECT " +
                " 		DISTINCT d.id " +
                " 		,'DOC_WORK_PERMIT' AS docType " +
                " 		,d.`user` " +
                " 		,d.document_name " +
                " 		,d.created_at " +
                " 		,d.approval_date " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT MIN(seq) " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 			) " +
                " 		END AS seq " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.approver_type " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_type " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.`user` " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_user " +
                " 	FROM doc_work_permit d " +
                " 	JOIN doc_approval a ON d.id = a.doc_work_permit_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " 	UNION ALL " +

                " 	SELECT " +
                " 		DISTINCT d.id " +
                " 		,'DOC_TIME_WORK_PERMIT' AS docType " +
                " 		,d.`user` " +
                " 		,d.document_name " +
                " 		,d.created_at " +
                " 		,d.approval_date " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT MIN(seq) " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_time_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 			) " +
                " 		END AS seq " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.approver_type " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_time_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_type " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.`user` " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_time_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_user " +
                " 	FROM doc_time_work_permit d " +
                " 	JOIN doc_approval a ON d.id = a.doc_time_work_permit_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " 	UNION ALL " +

                " 	SELECT " +
                " 		DISTINCT d.id " +
                " 		,'DOC_OBJECT_HANDLING_PLAN' AS docType " +
                " 		,d.`user` " +
                " 		,d.document_name " +
                " 		,d.created_at " +
                " 		,d.approval_date " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT MIN(seq) " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_object_handling_plan_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 			) " +
                " 		END AS seq " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.approver_type " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_object_handling_plan_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_type " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.`user` " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_object_handling_plan_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_user " +
                " 	FROM doc_object_handling_plan d " +
                " 	JOIN doc_approval a ON d.id = a.doc_object_handling_plan_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " 	UNION ALL " +

                " 	SELECT " +
                " 		DISTINCT d.id " +
                " 		,'DOC_SPACE_WORK_PERMIT' AS docType " +
                " 		,d.`user` " +
                " 		,d.document_name " +
                " 		,d.created_at " +
                " 		,d.approval_date " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT MIN(seq) " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_space_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 			) " +
                " 		END AS seq " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.approver_type " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_space_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_type " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.`user` " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_space_work_permit_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_user " +
                " 	FROM doc_space_work_permit d " +
                " 	JOIN doc_approval a ON d.id = a.doc_space_work_permit_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " 	UNION ALL " +

                " 	SELECT " +
                " 		DISTINCT d.id " +
                " 		,'DOC_RISK_ASSESSMENT_TABLE' AS docType " +
                " 		,d.`user` " +
                " 		,d.document_name " +
                " 		,d.created_at " +
                " 		,d.approval_date " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT MIN(seq) " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_risk_assessment_table_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 			) " +
                " 		END AS seq " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.approver_type " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_risk_assessment_table_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_type " +
                " 		,CASE " +
                " 			WHEN d.approval_date IS NOT NULL THEN NULL " +
                " 			ELSE ( " +
                " 				SELECT a2.`user` " +
                " 				FROM doc_approval a2 " +
                " 				WHERE a2.doc_risk_assessment_table_id = d.id " +
                " 				AND a2.approval_date IS NULL " +
                " 				ORDER BY a2.seq ASC " +
                " 				LIMIT 1 " +
                " 			) " +
                " 		END AS approver_user " +
                " 	FROM doc_risk_assessment_table d " +
                " 	JOIN doc_approval a ON d.id = a.doc_risk_assessment_table_id AND a.delete_yn = FALSE " +
                " 	JOIN projects p ON d.project = p.id AND p.company = " + companyId +
                " 	LEFT JOIN project_groups pg ON a.`user` = pg.`user` AND pg.project = " + projectId + " AND pg.delete_yn = FALSE AND pg.user_auth_type != 'TEAM_MASTER' " +
                " 	WHERE d.template_type = 'APPROVAL_DOCUMENT' " +
                " 	AND d.delete_yn = FALSE " +
                where01 +
                where02 +
                where03 +

                " ) d " +
                " JOIN users u ON d.user = u.id AND u.delete_yn = FALSE " +
                orderBy +
                " LIMIT " + ((page-1) * size) + "," + size;

        try {
            //System.out.println(listQuery);
            Map<String, Object> totalCnt = jdbcTemplate.queryForMap(countQuery);
            List<Map<String, Object>> list = jdbcTemplate.queryForList(listQuery);

            Map<String, Object> result = new HashMap<>();
            result.put("totalCnt", totalCnt.get("totalCnt"));
            result.put("list", list);

            return result;

        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }
}