package com.softj.pwg.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board extends Auditing{
    private String subject;
    private String nation;
    private String content;
    private Long view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Coment> coments = new ArrayList<>();


}


