package com.softj.pwg.controller;

import com.softj.pwg.entity.Board;
import com.softj.pwg.service.BoardService;
import com.softj.pwg.service.UserService;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;


    @GetMapping("/")
    public String index() throws Exception{
        return "index";
    }

    @GetMapping("/login")
    public String login() throws Exception{
        if(!Objects.isNull(AuthUtil.getLoginVO())){
            return "redirect:/";
        }
        return "sub/login";
    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(){
        AuthUtil.validate();
        return "redirect:/login";
    }
    //글 리스트
    @GetMapping("/board/list")//보드리스트 세션에 값이 담겨있어야함. 보드리스트에 바로 담겨있질 못하니깐
    public String boardList(ModelMap model, ParamVO params,
                            @PageableDefault(size = 20, sort = "seq", direction = Sort.Direction.DESC) Pageable pageable) throws Exception{ //modelandview랑 같은형식.
        model.addAttribute("list", boardService.boardList(params,pageable));
        model.addAttribute("nationName",params.getNationName());

        //list이름으로 view 페이지 사용가능

        //nation 값도 있고,
        return "sub/board-list";
    }
    //글 상세 페이지
    @GetMapping("/board/view")//view comment를 추가해줌
    public String boardView(ModelMap model,ParamVO params,Pageable pageable) throws Exception{
        model.addAttribute("view", boardService.boardView(params));
        model.addAttribute("comment", boardService.boardComent(params,pageable));
        model.addAttribute("likeCount", boardService.getBoardCount((Board)model.getAttribute("view")));
        model.addAttribute("isMyLike", boardService.isMyLike((Board)model.getAttribute("view")));
        return "sub/board-view";
    }

    // 글 쓰기, 글 수정 폼으로 가는 곳
    @GetMapping("/board/openWrite")
    public String boardOpenWrite(ModelMap model,ParamVO params) throws Exception{
        Board view = Board.builder().build();
        if(params.getSeq() != 0){ //boardseq 가 0이 아니면은 수정페이지.
            view = boardService.boardView(params);
        }
        model.addAttribute("view", view);
        return "sub/board-write";
    }

//    @GetMapping("/board/write")
//    public String boardWrite(ModelMap model,ParamVO params) throws Exception{
//
//        return "redirect:/board/list";
//    }

    @GetMapping("/mypage/alarm")
    public String mypageAlarm() {
        return "sub/mypage-alarm";
    }

    @GetMapping("/mypage/board")
    public String mypageBoard() throws Exception{
        return "sub/mypage-board";
    }

    @GetMapping("/mypage/like")
    public String mypageLike() throws Exception{
        return "sub/mypage-like";
    }

    @GetMapping("/search")
    public String search() throws Exception{
        return "sub/search";
    }

    @GetMapping("/nickName")
    public String nickName() throws Exception{
        return "sub/nickname";
    }


}
