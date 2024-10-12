package com.djz.exception;

import com.djz.response.ResultHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.UUID;

@Component
@Order(999)
@Slf4j
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    
    private static final String UN_KNOWN = "unknown";
    @Autowired
    private ObjectMapper objectMapper;

    public GlobalExceptionHandler() {
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        response.setContentType("application/json");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            String errorLogId = UUID.randomUUID() + "";
            String message;
            String code;
//            if (e instanceof AuthenticationException) {
//                code = 401;
//                if (StringUtils.isNotEmpty(e.getMessage())) {
//                    message = e.getMessage();
//                    log.error("status code: 401，认证错误：{}", message);
//                } else {
//                    message = e.toString();
//                    log.error("status code: 401，认证错误：", e);
//                }
//            } else if (e instanceof AuthorizationException) {
//                code = 403;
//                if (StringUtils.isNotEmpty(e.getMessage())) {
//                    message = e.getMessage();
//                    log.error("status code: 403，授权错误：{}", e.getMessage());
//                } else {
//                    message = e.getMessage();
//                    log.error("status code: 403，授权错误：", e);
//                }
//            } else

                if (e instanceof DuplicateKeyException) {
                code = String.valueOf(501);
                message = "请勿频繁操作";
                log.error("status code: " + code + "，重复错误：", e);
            } else if (e instanceof BusinessException) {
                code = String.valueOf(((BusinessException)e).getErrCode());
                if (StringUtils.isNotEmpty(e.getMessage())) {
                    message = e.getMessage();
                    log.error("status code: " + code + "，业务错误：{}", e.getMessage());
                } else {
                    message = e.getMessage();
                    log.error("status code: " + code + "，业务错误：", e);
                }
            } else {
                code = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
                message = "服务器出现错误，错误编号：" + errorLogId;
                if (StringUtils.isNotEmpty(e.getMessage())) {
                    message = e.getMessage();
                    log.error("status code: 500，编号：" + errorLogId + "，未知错误：", e);
                } else {
                    log.error("status code: 500，编号：" + errorLogId + "，未知错误：", e);
                }
            }

            //this.saveErrorLog(request, e, errorLogId);
            out.append(this.objectMapper.writeValueAsString(ResultHandler.ok(code, message, errorLogId)));
            response.setHeader("Access-Control-Allow-Origin", "*");
        } catch (IOException var13) {
            log.error("全局错误：", var13);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }

        }

        return new ModelAndView();
    }



    public static String getIP(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest is null");
        String ip = request.getHeader("X-Requested-For");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return StringUtils.isBlank(ip) ? null : ip.split(",")[0];
    }

    private byte[] getRequestBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        } else {
            byte[] buffer = new byte[contentLength];

            int readlen;
            for(int i = 0; i < contentLength; i += readlen) {
                readlen = request.getInputStream().read(buffer, i, contentLength - i);
                if (readlen == -1) {
                    break;
                }
            }

            return buffer;
        }
    }

    private String getRequestStr(HttpServletRequest request, byte[] buffer) throws IOException {
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }

        String str = (new String(buffer, charEncoding)).trim();
        if (StringUtils.isBlank(str)) {
            StringBuilder sb = new StringBuilder();
            Enumeration parameterNames = request.getParameterNames();

            while(parameterNames.hasMoreElements()) {
                String key = (String)parameterNames.nextElement();
                String value = request.getParameter(key);
                sb.append(key).append("=").append(value).append("&");
            }

            str = StringUtils.removeEnd(sb.toString(), "&");
        }

        return str.replaceAll("&amp;", "&");
    }
}
