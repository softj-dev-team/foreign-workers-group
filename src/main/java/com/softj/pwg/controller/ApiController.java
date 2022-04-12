package com.softj.pwg.controller;

import com.softj.pwg.entity.Board;
import com.softj.pwg.entity.Like;
import com.softj.pwg.entity.User;
import com.softj.pwg.service.BoardService;
import com.softj.pwg.service.UserService;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.util.CommonUtil;
import com.softj.pwg.vo.ParamVO;
import com.softj.pwg.vo.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final UserService userService;
    private final BoardService boardService;

    //로그인
    @RequestMapping("/login")
    public Response login(ParamVO params, MultipartHttpServletRequest request) throws Exception{
        return Response.builder()
                .data(userService.login(params, request))
                .build();
    }

    //닉네임 로그인
    @PostMapping("/setNickName")
    public Response setNickName(ParamVO params, MultipartHttpServletRequest request) throws Exception{
        return Response.builder()
                .data(userService.setNickName(params,request))
                .build();
    }
    //네이션 세션 저장
    @PostMapping("/setNation")//보드리스트
    public void setNation(ParamVO params) {
        AuthUtil.setAttr("nation", params.getNation()); //param에서 값가져옴
        AuthUtil.setAttr("nationName",params.getNationName());//세션에 저장하는거.

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
    public Response setCommentWrite(ParamVO params,@RequestParam(value = "isSecret",defaultValue = "false") boolean isSecret) {
        params.setSecret(isSecret);
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
    @PostMapping("/getPage") //게시글페이징.
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

    /**
     * 파일 다운로드
     * @param fileId
     * @throws Exception
     */
    @GetMapping("/comFileDownload/{path}.do")
    public void fileDownload(@PathVariable("path") String path,
                             HttpServletRequest request,
                             HttpServletResponse response, @RequestParam HashMap<String,String> search) throws Exception {
        path = path.replace("_","/");
        CommonUtil.setDisposition(path.substring(path.lastIndexOf("/") + 1), request, response);
        byte[] data = null;

        data = FileUtils.readFileToByteArray(new File(path));

        IOUtils.write(data, response.getOutputStream());
    }

    //섬머노트
    @PostMapping("/fileUpload")
    public Response fileUpload(ParamVO params, MultipartHttpServletRequest request) throws Exception{
        return Response.builder()
                .data(boardService.fileUpload(params, request))
                .build();
    }

    @PostMapping("/setNoticeBoard")
    public Response setNoticeBoard(ParamVO params){
        return Response.builder()
                .data(boardService.setNoticeBoard(params))
                .build();
    }
}
