package com.softj.pwg.service;

import com.softj.pwg.entity.User;
import com.softj.pwg.repo.UserRepo;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User login(ParamVO params) {
        //1.아이디 플랫폼 조회하기(가입여부)
        User user = null; //가입여부만확인함
        if(!StringUtils.isEmpty(params.getPlatform())) { //페이스북이면 아이디랑 플랫폼가져와서
            user = userRepo.findFirstByIdAndPlatform(params.getId(), params.getPlatform());//아이디 플랫폼 넣어서 조회
        }
        if(Objects.isNull(user)){ //가입이 안되어 있으면 user
            user = userRepo.save(User.builder()
                    .nickname(params.getNickname())
                    .id(params.getId()) //아이디
                    .platform(params.getPlatform())//플렛폼.
                    .build());//쿼리문날라가고

        }
        AuthUtil.setAttr("loginVO", user); //세션에 아이디랑 플렛폼 저장 처음에 초기값은 0,1로 되어있음
         //save하는데 안에서 user만들고 닉네임 넣었고 그걸 바로 세이브시켜라.
        return user;
    }

    public User setNickName(ParamVO params){ //여기서 세션에 저장된 값을 가져와서 꺼내서
        User user = (User)AuthUtil.getAttr("loginVO");//세션에 저장된 값을 가져옴loginvo
        user.setNickname(params.getNickname());
        return userRepo.save(user);
    }

}
