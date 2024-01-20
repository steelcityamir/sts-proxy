package com.stsproxy.api.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("[HTTP Request] Method: {}, URL: {}, IP: {}, User-Agent: {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr(), request.getHeader("User-Agent"));
        return true;
    }
}
