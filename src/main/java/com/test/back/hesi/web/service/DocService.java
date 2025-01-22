package com.test.back.hesi.web.service;

import com.test.back.hesi.web.dto.request.RequestCreateDocWorkPermit;
import com.test.back.hesi.web.dto.request.RequestSelectDocList;
import com.test.back.hesi.web.dto.response.ResponseApprovalDocDTO;
import com.test.back.hesi.web.dto.response.ResponseDocPlaceDTO;
import com.test.back.hesi.web.dto.response.ResponseDocWorkPermitDTO;
import com.test.back.hesi.web.dto.response.ResponseStorageDocDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DocService {
    /* 목록 */

    // 결재 문서함
    Page<ResponseApprovalDocDTO> getApprovalDocList(int page, int size, Long projectId, Long userId, RequestSelectDocList requestSelectDocList);
    Map<String, Object> getApprovalDocListV2(int page, int size, Long projectId, Long userId, RequestSelectDocList requestSelectDocList);
    // 문서 저장소
    Page<ResponseStorageDocDTO> getStorageDocList(int page, int size, Long projectId, RequestSelectDocList requestSelectDocList);
    // 작업허가서
    Page<ResponseDocWorkPermitDTO> getDocWorkPermitList(int page, int size, Long projectId, RequestSelectDocList requestSelectDocList);
    // 작업 장소
    List<ResponseDocPlaceDTO> getDocPlaceList(Long companyId);

    /* 상세 */

    // 작업허가서
    ResponseDocWorkPermitDTO getDocWorkPermitDetail(Long projectId, Long id);

    /* 생성, 복사, 수정 */

    // 작업허가서
    void processDocWorkPermit(RequestCreateDocWorkPermit dto, HttpServletRequest request);
    
    /* 삭제 */

    // 작업허가서
    void deleteDocWorkPermit(Long docId, Long projectId);

    /* 기타 */

    //List<ResponseApproverDTO> getApproverList(Long projectId, Long userId);
    //void processApproval(RequestApprovalDTO dto);
    //void processApprovalSignature(RequestApprovalSignatureDTO dto);
    //boolean checkApprovalDetailAuth(HashMap<String, String> map);
}
