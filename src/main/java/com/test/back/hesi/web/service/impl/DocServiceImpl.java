package com.test.back.hesi.web.service.impl;

import com.test.back.hesi.web.data.ProcessType;
import com.test.back.hesi.web.data.ProjectAuth;
import com.test.back.hesi.web.data.TemplateType;
import com.test.back.hesi.web.dto.request.RequestCreateDocWorkPermit;
import com.test.back.hesi.web.dto.request.RequestSelectDocList;
import com.test.back.hesi.web.dto.response.*;
import com.test.back.hesi.web.model.entity.*;
import com.test.back.hesi.web.repos.direct.DirectQuery;
import com.test.back.hesi.web.repos.jpa.DocPlaceRepos;
import com.test.back.hesi.web.repos.jpa.DocWorkPermitRepos;
import com.test.back.hesi.web.repos.jpa.ProjectRepos;
import com.test.back.hesi.web.repos.jpa.UserRepos;
import com.test.back.hesi.web.repos.jpa.dsl.custom.DocReposCustom;
import com.test.back.hesi.web.service.DocService;
import com.test.back.hesi.web.service.cmmn.UserJwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DocServiceImpl implements DocService {
    private final UserJwtService userJwtService;
    private final ProjectRepos projectRepository;
    private final DocWorkPermitRepos docWorkPermitRepos;

    private final DocPlaceRepos docPlaceRepos;
    private final ProjectRepos projectRepos;
    private final UserRepos userRepos;
    private final DocReposCustom docReposCustom;

    private final DirectQuery directQuery;

    @Override
    public Page<ResponseApprovalDocDTO> getApprovalDocList(
            int page, int size, Long projectId, Long userId,
            RequestSelectDocList requestSelectDocList) {

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Optional<Project> projectOptional = projectRepository.findByIdAndDeleteYnFalse(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();

            if (ProjectAuth.ADMIN.getValue().equals(project.getAuth())) {
                // ADMIN(관리자)
                return docReposCustom.getApprovalDocList(pageRequest, null, project.getCompany().getId(), userId, requestSelectDocList);
            } else {
                // SAFETY(안전기업), USER(협력사)
                return docReposCustom.getApprovalDocList(pageRequest, projectId, project.getCompany().getId(), userId, requestSelectDocList);
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getApprovalDocListV2(int page, int size, Long projectId, Long userId, RequestSelectDocList requestSelectDocList) {
        // 2024-02-01 : directQuery를 이용한 방식으로 수정
        Optional<Project> projectOptional = projectRepository.findByIdAndDeleteYnFalse(projectId);

        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();

            return directQuery.getApprovalDocList(page, size, project.getAuth(), project.getCompany().getId(), projectId, userId, requestSelectDocList);
        }
        return null;
    }

    @Override
    public Page<ResponseStorageDocDTO> getStorageDocList(int page, int size, Long projectId, RequestSelectDocList requestSelectDocList) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Optional<Project> projectOptional = projectRepository.findByIdAndDeleteYnFalse(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            if (ProjectAuth.ADMIN.getValue().equals(project.getAuth())) {
                return docReposCustom.getStorageDocList(pageRequest, null, project.getCompany().getId(), requestSelectDocList);
            }
            return docReposCustom.getStorageDocList(pageRequest, projectId, project.getCompany().getId(), requestSelectDocList);
        }
        return null;
    }

    @Override
    public Page<ResponseDocWorkPermitDTO> getDocWorkPermitList(int page, int size, Long projectId, RequestSelectDocList requestSelectDocList) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Optional<Project> projectOptional = projectRepository.findByIdAndDeleteYnFalse(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            return docReposCustom.getDocWorkPermitList(pageRequest, null, project.getCompany().getId(), requestSelectDocList);
            // 문서 작성함 리스트는 관리자, 프로젝트와 상관 없이 모두 조회
            /*if (ProjectAuth.ADMIN.getValue().equals(project.getAuth())) {
                return docReposCustom.getDocWorkPermitList(pageRequest, null, project.getCompany().getId(), requestSelectDocList);
            }
            return docReposCustom.getDocWorkPermitList(pageRequest, projectId, project.getCompany().getId(), requestSelectDocList);*/
        }
        return null;
    }

    @Override
    public List<ResponseDocPlaceDTO> getDocPlaceList(Long companyId) {
        return docPlaceRepos.findByCompanyIdAndDeleteYnFalseOrderByPlaceAsc(companyId);
    }

    @Override
    public ResponseDocWorkPermitDTO getDocWorkPermitDetail(Long projectId, Long id) {
        Optional<Project> projectOptional = projectRepository.findByIdAndDeleteYnFalse(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();

            DocWorkPermit docWorkPermit = docReposCustom.getDocWorkPermitDetail(id, null, project.getCompany().getId());

            // 문서 작성함 리스트는 관리자, 프로젝트와 상관 없이 모두 조회
            /*DocWorkPermit docWorkPermit;
            if (Objects.equals(project.getAuth(), ProjectAuth.ADMIN.getValue())) {
                docWorkPermit = docReposCustom.getDocWorkPermitDetail(id, null, project.getCompany().getId());
            } else {
                docWorkPermit = docReposCustom.getDocWorkPermitDetail(id, projectId, project.getCompany().getId());
            }*/

            ResponseDocPlaceDTO docPlaceDTO = ResponseDocPlaceDTO.builder()
                    .id(docWorkPermit.getWorkPlace().getId())
                    .place(docWorkPermit.getWorkPlace().getPlace())
                    .build();

            String approverName = "";
            Long approverId = 0L;

            List<ResponseDocApprovalDTO> approvals = new ArrayList<>();
            for (DocApproval approval : docWorkPermit.getApprovals()) {
                if (approval.getUser() != null) {
                    approverName = approval.getUser().getUserName();
                    approverId = approval.getUser().getId();
                }
                ResponseDocApprovalDTO approvalDTO = ResponseDocApprovalDTO.builder()
                        .id(approval.getId())
                        .approverType(approval.getApproverType())
                        .signature(approval.getSignature())
                        .opinion(approval.getOpinion())
                        .approverName(approverName)
                        .approverId(approverId)
                        .approvalDate(approval.getApprovalDate())
                        .seq(approval.getSeq())
                        .build();

                approvals.add(approvalDTO);
            }

            List<ResponseDocWorkPermitDetailDTO> details = new ArrayList<>();
            for (DocWorkPermitDetail detail : docWorkPermit.getDetails()) {
                ResponseDocWorkPermitDetailDTO detailDTO = ResponseDocWorkPermitDetailDTO.builder()
                        .id(detail.getId())
                        .seq(detail.getSeq())
                        .constructionName(detail.getConstructionName())
                        .workDetail(detail.getWorkDetail())
                        .worker(detail.getWorker())
                        .equipment(detail.getEquipment())
                        .mechanicalEquipmentStatus(detail.getMechanicalEquipmentStatus())
                        .build();

                details.add(detailDTO);
            }

            List<ResponseDocWorkPermitRiskDTO> risks = new ArrayList<>();
            for (DocWorkPermitRisk risk : docWorkPermit.getRisks()) {
                ResponseDocWorkPermitRiskDTO riskDTO = ResponseDocWorkPermitRiskDTO.builder()
                        .id(risk.getId())
                        .seq(risk.getSeq())
                        .workDetail(risk.getWorkDetail())
                        .riskFactor(risk.getRiskFactor())
                        .occurrenceFrequency(risk.getOccurrenceFrequency())
                        .riskIntensity(risk.getRiskIntensity())
                        .evaluationResult(risk.getEvaluationResult())
                        .riskReductionMeasure(risk.getRiskReductionMeasure())
                        .build();

                risks.add(riskDTO);
            }

            return ResponseDocWorkPermitDTO.builder()
                    .id(docWorkPermit.getId())
                    .documentName(docWorkPermit.getDocumentName())
                    .userId(docWorkPermit.getUser().getId())
                    .userName(docWorkPermit.getUser().getUserName())
                    .userImg(docWorkPermit.getUser().getImage())
                    .projectId(docWorkPermit.getProject().getId())
                    .projectName(docWorkPermit.getProject().getName())
                    .workPlace(docPlaceDTO)
                    .permissionPeriodStartAt(docWorkPermit.getPermissionPeriodStartAt())
                    .permissionPeriodEndAt(docWorkPermit.getPermissionPeriodEndAt())
                    .approvals(approvals)
                    .details(details)
                    .risks(risks)
                    .createdAt(docWorkPermit.getCreatedAt())
                    .build();
        }
        return null;
    }

    @Override
    public void processDocWorkPermit(RequestCreateDocWorkPermit dto, HttpServletRequest request) {
        if (dto.getProjectId() == null || dto.getProjectId() == 0) return;

        Optional<Project> projectOptional = projectRepository.findByIdAndDeleteYnFalse(dto.getProjectId());
        if (!projectOptional.isPresent()) return;

        /* 협력사 정보 */
        Project project = projectOptional.get();

        /* 작성자 정보 */
        Users user = userJwtService.getUserInfoByToken(request);

        // 작성자의 companyId와 프로젝트의 companyId가 다를 경우 return (COPY, UPDATE 시)
        if (!Objects.equals(project.getCompany().getId(), user.getCompany().getId())) return;

        /* 작업 장소 정보 */
        Optional<DocPlace> docPlaceOptional = docPlaceRepos.findById(dto.getDocPlaceId());
        if (!docPlaceOptional.isPresent()) return;
        DocPlace docPlace = docPlaceOptional.get();

        ProcessType process = dto.getProcess();
        DocWorkPermit docWorkPermit;
        List<DocWorkPermitDetail> details;

        // 생성, 복사
        if (ProcessType.CREATE == process || ProcessType.COPY == process) {
            docWorkPermit = DocWorkPermit.builder()
                    .documentName(dto.getDocumentName())
                    .project(project)
                    .user(user)
                    .workPlace(docPlace)
                    .permissionPeriodStartAt(dto.getPermissionPeriodStartAt())
                    .permissionPeriodEndAt(dto.getPermissionPeriodEndAt())
                    .templateType(TemplateType.DOCUMENT_STORAGE)
                    .build();

            /* 허가 작업내용 목록 */
            details = new ArrayList<>();
            for (int i = 0; i < dto.getDetails().size(); i++) {
                DocWorkPermitDetail detail = DocWorkPermitDetail.builder()
                        .docWorkPermit(docWorkPermit)
                        .seq(i + 1)
                        .constructionName(dto.getDetails().get(i).getConstructionName())
                        .workDetail(dto.getDetails().get(i).getWorkDetail())
                        .worker(dto.getDetails().get(i).getWorker())
                        .equipment(dto.getDetails().get(i).getEquipment())
                        .mechanicalEquipmentStatus(dto.getDetails().get(i).getMechanicalEquipmentStatus())
                        .build();

                details.add(detail);
            }
            docWorkPermit.setDetails(details);

            /* 작업 위험성 평가 목록 */
            List<DocWorkPermitRisk> risks = new ArrayList<>();
            if (dto.getRisks() != null) {
                for (int i = 0; i < dto.getRisks().size(); i++) {
                    DocWorkPermitRisk risk = DocWorkPermitRisk.builder()
                            .docWorkPermit(docWorkPermit)
                            .seq(i + 1)
                            .workDetail(dto.getRisks().get(i).getWorkDetail())
                            .riskFactor(dto.getRisks().get(i).getRiskFactor())
                            .occurrenceFrequency(dto.getRisks().get(i).getOccurrenceFrequency())
                            .riskIntensity(dto.getRisks().get(i).getRiskIntensity())
                            .evaluationResult(dto.getRisks().get(i).getEvaluationResult())
                            .riskReductionMeasure(dto.getRisks().get(i).getRiskReductionMeasure())
                            .build();

                    risks.add(risk);
                }
                docWorkPermit.setRisks(risks);
            }

            /* 결제 정보 목록 */
            List<DocApproval> docApprovalList = new ArrayList<>();
            if (dto.getApprovals() != null) {
                for (int i = 0; i < dto.getApprovals().size(); i++) {
                    DocApproval docApproval = DocApproval.builder()
                            .docWorkPermit(docWorkPermit)
                            .seq((i + 1) * 10)
                            .approverType(dto.getApprovals().get(i).getApproverType())
                            .build();

                    docApprovalList.add(docApproval);
                }
                docWorkPermit.setApprovals(docApprovalList);
            }
        } else {    // 수정
            docWorkPermit = docWorkPermitRepos.findByIdAndDeleteYn(dto.getId(), false);

            /* 허가 작업내용 목록 */
            details = docWorkPermit.getDetails();
            for (int i = 0; i < dto.getDetails().size(); i++) {
                DocWorkPermitDetail detail = details.get(i);
                detail.setConstructionName(dto.getDetails().get(i).getConstructionName());
                detail.setWorkDetail(dto.getDetails().get(i).getWorkDetail());
                detail.setWorker(dto.getDetails().get(i).getWorker());
                detail.setEquipment(dto.getDetails().get(i).getEquipment());
                detail.setMechanicalEquipmentStatus(dto.getDetails().get(i).getMechanicalEquipmentStatus());
            }

            /* 작업 위험성 평가 목록 */
            List<DocWorkPermitRisk> risks = docWorkPermit.getRisks();
            if (dto.getRisks() != null) {
                for (int i = 0; i < dto.getRisks().size(); i++) {
                    DocWorkPermitRisk risk = risks.get(i);
                    risk.setWorkDetail(dto.getRisks().get(i).getWorkDetail());
                    risk.setRiskFactor(dto.getRisks().get(i).getRiskFactor());
                    risk.setOccurrenceFrequency(dto.getRisks().get(i).getOccurrenceFrequency());
                    risk.setRiskIntensity(dto.getRisks().get(i).getRiskIntensity());
                    risk.setEvaluationResult(dto.getRisks().get(i).getEvaluationResult());
                    risk.setRiskReductionMeasure(dto.getRisks().get(i).getRiskReductionMeasure());
                }
            }

            /* 결제 정보 목록 */
            List<DocApproval> approvals = docWorkPermit.getApprovals();
            if (dto.getApprovals() != null) {
                for (int i = 0; i < dto.getApprovals().size(); i++) {
                    DocApproval approval = approvals.get(i);
                    approval.setApproverType(dto.getApprovals().get(i).getApproverType());
                }
            }

            docWorkPermit.setDocumentName(dto.getDocumentName());
            docWorkPermit.setProject(project);
            docWorkPermit.setWorkPlace(docPlace);
            docWorkPermit.setPermissionPeriodStartAt(dto.getPermissionPeriodStartAt());
            docWorkPermit.setPermissionPeriodEndAt(dto.getPermissionPeriodEndAt());
            docWorkPermit.setTemplateType(TemplateType.DOCUMENT_STORAGE);
            docWorkPermit.setApprovals(approvals);
            docWorkPermit.setDetails(details);
            docWorkPermit.setRisks(risks);

        }
        docWorkPermitRepos.save(docWorkPermit);
    }

    @Override
    public void deleteDocWorkPermit(Long docId, Long projectId) {
        DocWorkPermit docWorkPermit = docWorkPermitRepos.findByIdAndDeleteYn(docId, false);
        docWorkPermit.setDeleteYn(true);
    }


}

