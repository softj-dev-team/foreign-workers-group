package com.softj.pwg.config;

import com.softj.pwg.entity.IpBlock;
import com.softj.pwg.entity.User;
import com.softj.pwg.repo.IpBlockRepo;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.util.ClientIpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class IpBlockInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private IpBlockRepo ipBlockRepo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = ClientIpUtil.getClientIP(request);
        IpBlock block = ipBlockRepo.findFirstByIp(ip);
        if(Objects.nonNull(block)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
