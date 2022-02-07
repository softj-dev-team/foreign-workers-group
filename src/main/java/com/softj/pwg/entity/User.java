package com.softj.pwg.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends Auditing{
    private String nickname;
    private String id;
    private String platform;

//    @OneToMany(mappedBy = "board")
//    private List<Coment> coment;


}
