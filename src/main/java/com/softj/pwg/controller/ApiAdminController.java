package com.softj.pwg.controller;

import com.softj.pwg.service.ApiAdminService;
import com.softj.pwg.service.BoardService;
import com.softj.pwg.service.UserService;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.util.CommonUtil;
import com.softj.pwg.vo.ParamVO;
import com.softj.pwg.vo.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class ApiAdminController {
    private final ApiAdminService apiAdminService;
    private final UserService userService;
    private final BoardService boardService;

    //로그인
    @RequestMapping("/login")
    public Response login(ParamVO params) throws Exception{
        return Response.builder()
                .data(apiAdminService.adminLogin(params))
                .build();
    }
    //onoff
    @RequestMapping("/togglePublic")
    public Response togglePublic(ParamVO params) throws Exception{
        return Response.builder()
                .data(apiAdminService.togglePublic(params))
                .build();
    }
    //순서
    @RequestMapping("/updateSort")
    public Response updateSort(ParamVO params) throws Exception{
        return Response.builder()
                .data(apiAdminService.updateSort(params))
                .build();
    }
    //나라저장
    @RequestMapping("/saveNation")
    public Response saveNation(ParamVO params, MultipartHttpServletRequest request) throws Exception{
        return Response.builder()
                .data(apiAdminService.saveNation(params,request))
                .build();
    }
    //onoff
    @RequestMapping("/toggleUserBlock")
    public Response toggleUserBlock(ParamVO params) throws Exception{
        return Response.builder()
                .data(apiAdminService.toggleUserBlock(params))
                .build();
    }
    //삭제
    @RequestMapping("/deleteUsers")
    public Response deleteUsers(ParamVO params) throws Exception{
        apiAdminService.deleteUsers(params);
        return Response.builder()
                .build();
    }
    //삭제
    @RequestMapping("/deleteIps")
    public Response deleteIps(ParamVO params) throws Exception{
        apiAdminService.deleteIps(params);
        return Response.builder()
                .build();
    }
    //나라저장
    @RequestMapping("/saveIp")
    public Response saveIp(ParamVO params) {
        return Response.builder()
                .data(apiAdminService.saveIp(params))
                .build();
    }
}
