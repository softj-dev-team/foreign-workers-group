package com.softj.pwg.controller;

import com.softj.pwg.service.UserService;
import com.softj.pwg.vo.ParamVO;
import com.softj.pwg.vo.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final UserService userService;

    //로그인
    @PostMapping("/login")
    public Response login(ParamVO params) {
        return Response.builder()
                .data(userService.login(params))
                .build();
    }

    //닉네임 로그인
    @PostMapping("/setNickName")
    public Response setNickName(ParamVO params) {
        return Response.builder()
                .data(userService.setNickName(params))
                .build();
    }
}
