package com.namusd.jwtredis.model.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    public void createdDate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void modifiedDate() {
        this.updatedAt = LocalDateTime.now();
    }
}
