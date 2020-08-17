

package com.mybatis.plus.config.oauth2;


import org.apache.shiro.authc.AuthenticationToken;

/**
 *
 * 描述: Token
 *
 * @author 官萧何
 * @date 2020/8/17 11:19
 * @version V1.0
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
