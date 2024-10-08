package com.akhrullo.webchat.model.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5678314898055984358L;

    @CreatedDate
    @Column(updatable = false, name = "created_time")
    private LocalDateTime createdTime;

    @Setter
    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
}