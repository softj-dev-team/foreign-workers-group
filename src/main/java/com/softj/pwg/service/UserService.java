package com.softj.pwg.service;

import com.softj.pwg.entity.User;
import com.softj.pwg.repo.UserRepo;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User anonymousLogin(ParamVO params){
        User save = userRepo.save(User.builder()
                .nickname(params.getNickname())
                .build());
        AuthUtil.setAttr("loginVO", save);
        return save;
    }
}
