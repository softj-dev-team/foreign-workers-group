package com.softj.pwg.controller;

import com.google.common.hash.Hashing;
import com.softj.pwg.entity.IpBlock;
import com.softj.pwg.entity.Nation;
import com.softj.pwg.entity.User;
import com.softj.pwg.repo.IpBlockRepo;
import com.softj.pwg.repo.NationRepo;
import com.softj.pwg.service.BoardService;
import com.softj.pwg.service.IpBlockService;
import com.softj.pwg.service.UserService;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final BoardService boardService;
    private final UserService userService;
    private final IpBlockService ipBlockService;
    private final NationRepo nationRepo;
    private final IpBlockRepo ipBlockRepo;

    @RequestMapping("/admin")
    public String index(ModelMap model){
        model.addAttribute("list",nationRepo.findAllByOrderBySortAsc());
        return "admin/index";
    }
    @RequestMapping("/admin/ip-add")
    public String ipAdd(ModelMap model, ParamVO params){
        IpBlock entity = IpBlock.builder().build();
        if(params.getSeq() != 0){
            entity = ipBlockRepo.findById(params.getSeq()).get();
        }
        model.addAttribute("el",entity);
        return "admin/ip-add";
    }
    @RequestMapping("/admin/ip-block")
    public String ipBlock(ModelMap model, ParamVO params, Pageable p){
        model.addAttribute("list", ipBlockService.getIpList(params, p));
        return "admin/ip-block";
    }
    @RequestMapping("/admin/list-add")
    public String listAdd(ModelMap model, ParamVO params){
        Nation entity = Nation.builder().build();
        if(params.getSeq() != 0){
            entity = nationRepo.findById(params.getSeq()).get();
        }
        model.addAttribute("el",entity);
        return "admin/list-add";
    }
    @RequestMapping("/admin/user-block")
    public String userBlock(ModelMap model, ParamVO params, Pageable p){
        model.addAttribute("list",userService.getUserList(params, p));
        return "admin/user-block";
    }
    @RequestMapping("/admin/login")
    public String adminLogin(ModelMap model, ParamVO params){
        if(Objects.nonNull(AuthUtil.getAdminVO())){
            return "redirect:/admin";
        }
        return "admin/login";
    }
    @RequestMapping("/admin/logout")
    public String adminLogout(ModelMap model, ParamVO params){
        AuthUtil.invalidate("adminVO");
        AuthUtil.invalidate();
        return "redirect:/admin/login";
    }
}
