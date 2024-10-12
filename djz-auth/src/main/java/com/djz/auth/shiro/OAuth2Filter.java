package com.djz.auth.shiro;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 授权filter
 *
 * @author hgsoft
 */
@Data
@Slf4j
public class OAuth2Filter extends AuthenticatingFilter implements InitializingBean {

    private String resourceId;

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String accessToken = httpRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = "null";
        } // 使用jwt解析
        return new JWTToken(accessToken);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {


        try {
            OAuthResponse oAuthResponse = OAuthResponse.errorResponse(200)
                    .setError(OAuthError.ResourceResponse.INVALID_ACCESS_TOKEN).setErrorDescription(ae.getMessage())
                    .buildJSONMessage();
            writeOAuthJsonResponse((HttpServletResponse) response, oAuthResponse);
        } catch (OAuthSystemException e) {
            log.error("Build JSON message error", e);
            throw new IllegalStateException(e);
        }

        return false;
    }

    @Override
    public void afterPropertiesSet() {
    }


    @SuppressWarnings("rawtypes")
    public static void writeOAuthJsonResponse(HttpServletResponse response, OAuthResponse oAuthResponse) {

        final int responseStatus = oAuthResponse.getResponseStatus();
        try {
            final Map<String, String> headers = oAuthResponse.getHeaders();
            for (String key : headers.keySet()) {
                response.addHeader(key, headers.get(key));
            }
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType(OAuth.ContentType.JSON);
            response.setStatus(responseStatus);

            final PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            Map map = gson.fromJson(oAuthResponse.getBody(), Map.class);
            out.print(gson.toJson(map));
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Write OAuthResponse error", e);
        }
    }
}
