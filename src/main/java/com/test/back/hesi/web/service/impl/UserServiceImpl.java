package com.test.back.hesi.web.service.impl;

import com.querydsl.core.util.StringUtils;
import com.test.back.hesi.utils.DateUtil;
import com.test.back.hesi.utils.PasswordUtil;
import com.test.back.hesi.utils.UserAuthUtil;
import com.test.back.hesi.web.data.UserType;
import com.test.back.hesi.web.data.YN;
import com.test.back.hesi.web.dto.request.RequestUserDTO;
import com.test.back.hesi.web.model.cmmn.BfListResponse;
import com.test.back.hesi.web.model.cmmn.BfPage;
import com.test.back.hesi.web.model.cmmn.BfToken;
import com.test.back.hesi.web.model.doc.LoginHistory;
import com.test.back.hesi.web.model.entity.Company;
import com.test.back.hesi.web.model.entity.Project;
import com.test.back.hesi.web.model.entity.ProjectGroup;
import com.test.back.hesi.web.model.entity.Users;
import com.test.back.hesi.web.repos.jpa.CompanyRepos;
import com.test.back.hesi.web.repos.jpa.ProjectGroupRepos;
import com.test.back.hesi.web.repos.jpa.ProjectRepos;
import com.test.back.hesi.web.repos.jpa.UserRepos;
import com.test.back.hesi.web.repos.mongo.LoginHistoryRepos;
import com.test.back.hesi.web.service.UserService;
import com.test.back.hesi.web.service.cmmn.UserJwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.test.back.hesi.web.data.UserAuthType.TEAM_USER;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepos userRepos;
    private final ProjectRepos projectRepos;
    private final ProjectGroupRepos projectGroupRepos;
    private final CompanyRepos companyRepos;
    private final PasswordUtil passwordUtil;
    private final DateUtil dateUtil;
    private final LoginHistoryRepos loginHistoryRepos;
    private final UserJwtService userJwtService;
    private final UserAuthUtil userAuthUtil;

    @Override
    public Users add(Users obj, HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public Users edit(Users obj, HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public void remove(long seq, HttpServletRequest httpServletRequest) throws Exception {

    }

    @Override
    public Users find(long seq, HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public BfListResponse<Users> findAll(Users obj, BfPage bfPage, HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public Users generate(Users obj) {
        return null;
    }

    @Override
    public Users findByEmailAndUserNameAndDeleteYnFalse(String email, String userName) {
        Users user = userRepos.findByEmailAndUserNameAndDeleteYnFalse(email, userName);
        if(Objects.isNull(user)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "해당 유저를 찾을 수 없습니다.");
        }

        return user;
    }

    @Override
    public boolean findByUserIdAndEmailAndUserNameAndDeleteYnFalse(String userId, String email, String userName) {
        Users user = userRepos.findByUserIdAndEmailAndUserNameAndDeleteYnFalse(userId, email, userName);

        if(Objects.isNull(user)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "해당 유저를 찾을 수 없습니다.");

        }else {
            return true;
        }
    }

    @Transactional
    @Override
    public Users modifyPassword(String password, String newpassword, HttpServletRequest httpServletRequest) throws Exception {
        Users myInfo = userJwtService.getUserInfoByToken(httpServletRequest);

        if (!passwordUtil.checkEqual(myInfo.getPassword(), password)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "기존 비밀번호가 일치하지 않습니다.");
        }

        myInfo.setPassword(passwordUtil.encode(newpassword));
        return userRepos.save(myInfo);
    }

    @Transactional
    @Override
    public Users modifyPasswordNotLogin(String userId, String newpassword, HttpServletRequest httpServletRequest) throws Exception{
        Users user = userRepos.findByUserId(userId);
        if(user == null){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        user.setPassword(passwordUtil.encode(newpassword));
        return userRepos.save(user);
    }

    @Override
    public Boolean getEmailDuplicate(String email){
        Users user = userRepos.findUsersByEmail(email);
        return user != null;
    }

    @Transactional
    @Override
    public void add(RequestUserDTO dto) throws Exception {
        Users user = this.toEntity(dto);

        if (Objects.isNull(user)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }

        if (user.getType() == UserType.ADMIN) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "관리자는 가입할 수 없습니다.");
        }

        if(StringUtils.isNullOrEmpty(user.getUserId())){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요.");
        }

        if(!checkUserId(user.getUserId())){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }

        if(!StringUtils.isNullOrEmpty(user.getPhoneNo())){
            if(!checkUserPhoneNo(user.getPhoneNo())){
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "중복된 휴대폰번호입니다.");
            }
        }

        user.setType(UserType.NORMAL);
        user.setDeleted(YN.N);

        if (user.getMarketingAllowed() == YN.Y) {
            user.setMarketingAllowedAt(dateUtil.getThisTime());
        }

        if (user.getMessageAllowed() == YN.Y) {
            user.setMessageAllowedAt(dateUtil.getThisTime());
        }

        user.setPassword(passwordUtil.encode(user.getPassword()));

        Company company = companyRepos.findById(dto.getCompany()).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "회사를 조회할 수 없습니다."));
        user.setCompany(company);

        // INSERT users
        Users userInfo = userRepos.save(user);

        /* 프로젝트 그룹원 등록 */
        Project project = projectRepos.findById(dto.getProject()).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "프로젝트를 조회할 수 없습니다."));

        // INSERT project_groups
        ProjectGroup newProjectGroup = ProjectGroup
            .builder()
            .userAuthType(TEAM_USER)
            .project(project)
            .user(userInfo)
            .name(project.getName())
            .build();
        projectGroupRepos.save(newProjectGroup);
    }

    @Override
    public BfToken login(Users user, HttpServletRequest httpServletRequest) throws Exception {
        boolean isSuccess = false;
        String errorMsg = "";

        String template = "Addr:" + httpServletRequest.getRemoteAddr()
            + "/Host:" + httpServletRequest.getRemoteHost()
            + "/User:" + httpServletRequest.getRemoteUser();

        // 유저 정보 조회
        Users userInfos = userRepos.findByUserId(user.getUserId());

        if (Objects.isNull(userInfos) || userInfos.getDeleted() == YN.Y) {
            // 유저 정보가 없거나, 삭제된 유저인 경우
            errorMsg = "정보가 없습니다.";

        }else if(!passwordUtil.checkEqual(userInfos.getPassword(), user.getPassword())) {
            // 비밀번호가 일치하지 않은 경우
            errorMsg = "비밀번호가 틀립니다.";

        }else if(!userInfos.getUseYn()) {
            // 비활성화된 계정인 경우
            errorMsg = "비활성화된 계정입니다.";

        }else {
            isSuccess = true;
        }

        // 로그인 시도 이력 저장(mongodb)
        loginHistoryRepos.save(LoginHistory.builder()
            .createDt(LocalDateTime.now())
            .isSuccess(isSuccess)
            .userId(user.getUserId())
            .password(user.getPassword())
            .platformInfo(template)
            .build());

        if(!isSuccess) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, errorMsg);
        }

        BfToken token = userJwtService.generateToken(userInfos, template);

        BfToken result = new BfToken();
        result.setAccessToken(token.getAccessToken());
        result.setEmail(token.getEmail());
        result.setId(token.getId());
        result.setImage(token.getImage());
        result.setSnsType(token.getSnsType());
        result.setType(token.getType());
        result.setUserId(token.getUserId());
        result.setUserName(token.getUserName());

        result.setCompany(
            Company
                .builder()
                .id(userInfos.getCompany().getId())
                .build()
        );

        result.setAuth(userAuthUtil.getProjectAuth(userInfos));

        return result;
    }

    @Override
    public boolean checkUserId(String userId) {
        Users userInfos = userRepos.findByUserId(userId);
        return Objects.isNull(userInfos);
    }

    private boolean checkUserPhoneNo(String phoneNo) {
        List<Users> userList = userRepos.findByPhoneNoAndDeleteYnFalse(phoneNo);
        return (userList.size() == 0);
    }

    public Users toEntity (RequestUserDTO dto){
        Users user = new Users();
        user.setType(dto.getType());
        user.setUserId(dto.getUserId());
        user.setPhoneNo(dto.getPhoneNo());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setSnsAllowed(dto.getSnsAllowed());
        user.setMarketingAllowed(dto.getMarketingAllowed());
        user.setMarketingAllowedAt(dto.getMarketingAllowedAt());
        user.setMessageAllowed(dto.getMessageAllowed());
        user.setMessageAllowedAt(dto.getMessageAllowedAt());
        user.setPassword(dto.getPassword());
        user.setSnsType(dto.getSnsType());

        return user;
    }
}