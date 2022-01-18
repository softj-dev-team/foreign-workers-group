package com.softj.pwg.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public abstract class Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long seq;

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(updatable = true)
    protected LocalDateTime updatedAt;

    @Column(updatable = true)
    protected boolean isDel;

    @Column(updatable = false)
    protected String createdId;

    @Column(updatable = true)
    protected String updatedId;

    public Auditing(long seq, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDel, String createdId, String updatedId) {
        this.seq = seq;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDel = isDel;
        this.createdId = createdId;
        this.updatedId = updatedId;
    }
}