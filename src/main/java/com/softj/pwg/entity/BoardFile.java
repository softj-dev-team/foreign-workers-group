package com.softj.pwg.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "board_file")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class BoardFile extends Auditing{
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    private String fileName;

}


