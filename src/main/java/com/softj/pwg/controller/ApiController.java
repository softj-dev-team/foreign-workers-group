package com.softj.pwg.controller;

import com.softj.pwg.entity.Board;
import com.softj.pwg.entity.Like;
import com.softj.pwg.entity.User;
import com.softj.pwg.service.BoardService;
import com.softj.pwg.service.UserService;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import com.softj.pwg.vo.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    //네이션 세션 저장
    @PostMapping("/setNation")//보드리스트
    public void setNation(ParamVO params) {
        AuthUtil.setAttr("nation", params.getNation()); //param에서 값가져옴

    }
    //글 작성, 글 수정 완료 db 등록
    @PostMapping("/setBoardWrite")
    public Response setBoardWrite(ParamVO params) {
        return Response.builder()
                .data(boardService.boardWrite(params))
                .build();

    }
    //댓글 작성, 수정 완료
    @PostMapping("/setCommentWrite")
    public Response setCommentWrite(ParamVO params) {
        return Response.builder()
                .data(boardService.comentWrite(params))
                .build();

    }
    @PostMapping("/setRemoveComent") //댓글삭제
    public void setRemoveComent(ParamVO params) {
         boardService.deleteComment(params.getSeq());//type long . 이미 내장된 것.primary key.

    }

    @PostMapping("/setRemoveBoard") //게시글 삭제
    public void setRemoveBoard(ParamVO params) {
        boardService.deleteBoard(params.getSeq());

    }

    @PostMapping("/setBoardLike") //좋아요,
    public Response setLike(ParamVO params) {
        boardService.likeBoard(params);
        return Response.builder()
                .data("OK")
                .build();
    }
    @PostMapping("/getPage") //페이징
    public Response getPage(ParamVO params,Pageable pageable) {
        return Response.builder()
                .data(boardService.boardList(params,pageable))
                .build();

    }
    @PostMapping("/getCommentPage") //페이징
    public Response getCommentPage(ParamVO params,Pageable pageable) {
        return Response.builder()
                .data(boardService.boardComent(params, pageable))
                .build();

    }
}
