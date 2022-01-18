package com.softj.pwg.config;

import com.softj.pwg.entity.User;
import com.softj.pwg.util.AuthUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class PwgInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User u = AuthUtil.getLoginVO();
        if(Objects.isNull(u)){
            response.sendRedirect("/login");
            return false;
        }else if(request.getRequestURI().contains("mypage") && StringUtils.isEmpty(u.getId())){
            response.sendRedirect("/");
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
