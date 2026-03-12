package com.localpayment.lp_test_logs.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LogsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader("x-trace-id");
        String intenalId = request.getHeader("x-internal-id");
        request.setAttribute("startTime", System.currentTimeMillis());
        MDC.put("trace_id", traceId);
        MDC.put("internal_id", intenalId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;
        //log.info("Tiempo de procesamiento de la solicitud: {} milisegundos", processingTime);
        MDC.remove("trace_id");
        MDC.remove("internal_id");
    }
}