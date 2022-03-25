package com.softj.pwg.config;

import com.softj.pwg.util.AuthUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AdminInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(Objects.isNull(AuthUtil.getAdminVO())){
            response.sendRedirect("/admin/login");
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
