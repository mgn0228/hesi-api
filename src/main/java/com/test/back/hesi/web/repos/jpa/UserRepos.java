package com.test.back.hesi.web.repos.jpa;

import com.test.back.hesi.web.data.SNSType;
import com.test.back.hesi.web.data.YN;
import com.test.back.hesi.web.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepos extends JpaRepository<Users, Long> {
    Users findByUserId(String userId);
    Users findByUserNameAndPhoneNoAndDeleted(String userId, String phoneNo, YN deleted);
    Users findUsersBySnsValueAndSnsType(String snsValue, SNSType snsType);
    Users findUsersByEmail(String email);
    List<Users> findByPhoneNoAndDeleteYnFalse(String phoneNo);
    Users findByEmailAndUserNameAndDeleteYnFalse(String email, String userName);
    Users findByUserIdAndEmailAndUserNameAndDeleteYnFalse(String userId, String email, String userName);

    Users findByIdAndDeleteYn(Long id, Boolean deleteYn);
}