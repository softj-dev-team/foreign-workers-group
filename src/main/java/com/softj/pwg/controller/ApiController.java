package com.softj.pwg.controller;

import com.softj.pwg.service.BoardService;
import com.softj.pwg.service.UserService;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import com.softj.pwg.vo.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final UserService userService;
    private final BoardService boardService;

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
    @PostMapping("/setNation")//보드리스트
    public void setNation(ParamVO params) {
        AuthUtil.setAttr("nation", params.getNation()); //param에서 값가져옴

    }
    @PostMapping("/setBoardWrite")
    public Response setBoardWrite(ParamVO params) {
        return Response.builder()
                .data(boardService.boardWrite(params))
                .build();

    }
    @PostMapping("/setCommentWrite")
    public Response setCommentWrite(ParamVO params) {
        return Response.builder()
                .data(boardService.comentWrite(params))
                .build();

    }
    @PostMapping("/setCommentRemove")
    public void setCommentRemove(ParamVO params) {
                boardService.comentRemove(params);
    }


//    @PostMapping("/setBoardComment")//보드리스트
//    public Response setBoardComment(ParamVO params) {
//        return Response.builder()
//                .data(boardService.BoardComment(params))
//                .build();
//
//    }

}
