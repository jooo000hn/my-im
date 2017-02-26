package com.goku.oauth.provider.token;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

/**
 * 该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的.
 具体实现请参考DefaultClientKeyGenerator.java类.
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public class SECAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {
	
    public String extractKey(OAuth2Authentication authentication) {
        if( null == authentication){
            return null;
        }
        return super.extractKey(authentication);
    }
}