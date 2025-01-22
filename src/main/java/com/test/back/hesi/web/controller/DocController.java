package com.test.back.hesi.web.controller;

import com.test.back.hesi.utils.ResponseUtil;
import com.test.back.hesi.web.dto.request.RequestCreateDocWorkPermit;
import com.test.back.hesi.web.dto.request.RequestSelectDocList;
import com.test.back.hesi.web.dto.response.ResponseDocPlaceDTO;
import com.test.back.hesi.web.service.DocService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/doc")
@RequiredArgsConstructor
//@Api(tags = {"Docs"}, description = "문서")
@Slf4j
public class DocController {

    private final DocService docService;

    // /docs/list/doc-work-permit
    @GetMapping(value = "/list/doc-work-permit")
    //@ApiOperation(value = "작업허가서 목록 조회")
    public ResponseEntity<?> getDocWorkPermitList(
            @RequestParam(name = "page")  int page, //@ApiParam(value = "페이지 번호", required = true)
            @RequestParam(name = "size")  int size, //@ApiParam(value = "조회할 목록 개수", required = true)
            @RequestParam(name = "projectId") Long projectId, //@ApiParam(value = "프로젝트 pk", required = true)
            RequestSelectDocList requestSelectDocList) {

        return ResponseUtil.sendResponse(docService.getDocWorkPermitList(page, size, projectId, requestSelectDocList));
    }

    // /docs/detail/doc-work-permit
    @GetMapping(value = "/detail/doc-work-permit")
    //@ApiOperation(value = "작업허가서 상세 조회")
    public ResponseEntity<?> getDocWorkPermitDetail(
            @RequestParam(name = "projectId") Long projectId, //@ApiParam(value = "프로젝트 pk", required = true)
            @RequestParam(name = "id") Long id) { //@ApiParam(value = "작업허가서 pk", required = true)

        return ResponseUtil.sendResponse(docService.getDocWorkPermitDetail(projectId, id));
    }

    @GetMapping(value = "/list/place")
    //@ApiOperation(value = "작업 장소 목록 조회")
    public ResponseEntity<?> getDocPlaceList(
            @RequestParam(name = "companyId") Long companyId) {//@ApiParam(value = "회사 pk", required = true)
        Map<String, List<ResponseDocPlaceDTO>> result = new HashMap<>();
        result.put("placeList", docService.getDocPlaceList(companyId));
        return ResponseUtil.sendResponse(result);
    }

    @PostMapping(value = "/create/doc-work-permit")
    //@ApiOperation(value = "작업허가서 등록")
    public ResponseEntity<?> createDocWorkPermit(
            @RequestBody RequestCreateDocWorkPermit dto,
            HttpServletRequest httpServletRequest) {
        docService.processDocWorkPermit(dto, httpServletRequest);
        return new ResponseEntity<>(OK);
    }

    @GetMapping(value = "/list/storage")
    //@ApiOperation(value = "문서 저장소 목록 조회")
    public ResponseEntity<?> getStorageDocList(
            @RequestParam(name = "page") int page, //@ApiParam(value = "페이지 번호", required = true)
            @RequestParam(name = "size") int size, //@ApiParam(value = "조회할 목록 개수", required = true)
            @RequestParam(name = "projectId") Long projectId, //@ApiParam(value = "프로젝트 pk", required = true)
            RequestSelectDocList requestSelectDocList) {

        return ResponseUtil.sendResponse(docService.getStorageDocList(page, size, projectId, requestSelectDocList));
    }
}
