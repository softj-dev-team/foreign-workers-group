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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class User extends Auditing{
    private String nickname;
    private String id;
    private String platform;
    private String image;
    private boolean isAdmin;
    private boolean isBlock;
    private String password;

//    @OneToMany(mappedBy = "board")
//    private List<Coment> coment;


}
