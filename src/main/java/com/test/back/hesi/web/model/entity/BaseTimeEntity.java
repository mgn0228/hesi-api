package com.test.back.hesi.web.model.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "createdAt", columnDefinition = "datetime(6) COMMENT '등록일시'", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", columnDefinition = "datetime(6) COMMENT '(마지막)수정일시'", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "delete_yn", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private Boolean deleteYn = false;

}