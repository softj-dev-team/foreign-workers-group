package com.softj.pwg.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "coment")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class Coment extends Auditing{
    private String content;
    private Long upperSeq;
    private boolean isSecret;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_seq")
    private Board board;

    @Transient
    private List<Coment> children = new ArrayList<>();
}
