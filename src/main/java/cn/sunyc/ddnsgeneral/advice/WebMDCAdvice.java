package cn.sunyc.ddnsgeneral.advice;

import cn.sunyc.ddnsgeneral.utils.MDCUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 用来给所有接口自动添加MDC并且打印日志的切面
 *
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/5 10:59
 */
@Aspect
@Slf4j
@Component
public class WebMDCAdvice {

    @Pointcut("execution(public * cn.sunyc.ddnsgeneral.controller.*.*(..))")
    public void webLog() {
    }

    @Around("webLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MDCUtil.setRequestId();
        try {
            final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return null;
            }
            final HttpServletRequest request = attributes.getRequest();
            final StringBuilder headerInfo = new StringBuilder();
            final Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headerInfo.append(headerName).append(":").append(request.getHeader(headerName)).append(",");
            }
            final Object[] args = joinPoint.getArgs();
            log.info("accept_request" +
                    "\tip:" + request.getRemoteAddr() +
                    "\tpath:" + request.getMethod() + " " + request.getRequestURL().toString() +
                    "\tparams:" + Arrays.toString(args) +
                    "\theaders:" + headerInfo);

            final Object proceed = joinPoint.proceed();
            log.info("return_response" +
                    "\tip:" + request.getRemoteAddr() +
                    "\tpath:" + request.getMethod() + " " + request.getRequestURL().toString() +
                    "\tresponse:" + (proceed == null ? null : (proceed.toString().length() > 1000 ? proceed.toString().substring(0, 1000) : proceed.toString()))
            );

            return proceed;
        } finally {
            MDCUtil.clearRequestId();
        }
    }
}
