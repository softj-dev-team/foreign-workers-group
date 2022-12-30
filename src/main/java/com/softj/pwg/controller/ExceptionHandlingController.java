package com.softj.pwg.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class ExceptionHandlingController extends AbstractErrorController {
    // 에러 페이지 정의
    private final String ERROR_404_PAGE_PATH = "error-404";
    private final String ERROR_403_PAGE_PATH = "error-403";
    private final String ERROR_500_PAGE_PATH = "error-500";
    private final String ERROR_ETC_PAGE_PATH = "error-etc";

    public ExceptionHandlingController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @ResponseBody
    @RequestMapping(value = "/error")
    public Map handleError(HttpServletRequest request, Model model) {
        Map<String, Object> result = new HashMap<>();
        // 에러 코드를 획득한다.
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Exception e = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String message = e.getMessage();
        try{
            message = message.split(":")[1];
        }catch (Exception ex){
            message = "";
        }
        result.put("message", message);

        // 에러 코드에 대한 상태 정보
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
        if (status != null) {
            // HttpStatus와 비교해 페이지 분기를 나누기 위한 변수
            int statusCode = Integer.valueOf(status.toString());
            // 로그로 상태값을 기록 및 출력
            log.info("httpStatus : {}", statusCode);
            // 404 error
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                result.put("code", ERROR_404_PAGE_PATH);
            } // 500 error
            // 403 error
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                result.put("code", ERROR_403_PAGE_PATH);
            } // 500 error
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                result.put("code", ERROR_500_PAGE_PATH);
            }
        }
        // 정의한 에러 외 모든 에러는 error/500 페이지로 보낸다.
        return result;
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}

