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
        if(Objects.isNull(u) || u.getSeq() == 0) {
            if(request.getRequestURI().startsWith("/api")){
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
            response.sendRedirect("/login");
            return false;
        }
//        }else if(request.getRequestURI().contains("mypage") && StringUtils.isEmpty(u.getId())){
//            response.sendRedirect("/board/list");
//            return false;
//        }
        return super.preHandle(request, response, handler);
    }
}
