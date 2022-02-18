package com.softj.pwg.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class Board extends Auditing{
    private String subject;
    private String nation;
    private String content;
    private long views;
//    @Column(name="user_seq")
//    private long userSeq;

    //조회수 로직
    public void increaseViewCount(){
        this.views++;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "user_seq")
    private User user;

//    @OneToMany(mappedBy = "board")
//    private List<Coment> coment;

}


