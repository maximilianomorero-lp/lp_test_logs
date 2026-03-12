package com.localpayment.lp_test_logs.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class CustomExceptionHandler implements HandlerExceptionResolver {

    @SneakyThrows
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //log.error("[log_name: exception] " + getStackTraceAsString(ex));
        ex.setStackTrace(new StackTraceElement[0]);;
        throw ex;
    }

    public String getStackTraceAsString(Throwable throwable) {
        if(throwable == null || throwable.getStackTrace().length == 0){
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
