package com.luckyparty.auth.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 일시")
    @CreatedDate
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    @Comment("수정 일시")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
