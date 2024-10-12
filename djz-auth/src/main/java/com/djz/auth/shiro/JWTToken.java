package com.djz.auth.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author djz
 * @since 2022-10-21
 */
public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
