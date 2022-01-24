package com.softj.pwg.controller;

import com.softj.pwg.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class BoardController {


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

    @GetMapping("/boardList")
    public String boardList() throws Exception{
        return "sub/board-list";
    }
    @GetMapping("/boardView")
    public String boardView() throws Exception{
        return "sub/board-view";
    }

    @GetMapping("/boardWrite")
    public String boardWrite() throws Exception{
        return "sub/board-write";
    }

    @GetMapping("/mypageAlarm")
    public String mypageAlarm() {
        return "sub/mypage-alarm";
    }

    @GetMapping("/mypageBoard")
    public String mypageBoard() throws Exception{
        return "sub/mypage-board";
    }

    @GetMapping("/mypageLike")
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
