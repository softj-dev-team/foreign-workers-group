package com.softj.pwg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping("login")
    public String login() throws Exception{
        return "sub/login";
    }

    @GetMapping("boardList")
    public String boardList() throws Exception{
        return "sub/board-list";
    }
    @GetMapping("boardView")
    public String boardView() throws Exception{
        return "sub/board-view";
    }

    @GetMapping("boardWrite")
    public String boardWrite() throws Exception{
        return "sub/board-write";
    }

    @GetMapping("mypageAlarm")
    public String mypageAlarm() {
        return "sub/mypage-alarm";
    }

    @GetMapping("mypageBoard")
    public String mypageBoard() throws Exception{
        return "sub/mypage-board";
    }

    @GetMapping("mypageLike")
    public String mypageLike() throws Exception{
        return "sub/mypage-like";
    }

    @GetMapping("search")
    public String search() throws Exception{
        return "sub/search";
    }


}
