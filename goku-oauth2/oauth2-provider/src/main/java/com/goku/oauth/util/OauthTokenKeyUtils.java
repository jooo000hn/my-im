package com.goku.oauth.util;


import com.goku.oauth.Constants;
import com.goku.redis.serializer.StringRedisSerializer;

/**
 * User: user
 * Date: 15/11/26
 * Version: 1.0
 */
public class OauthTokenKeyUtils {
    private final static StringRedisSerializer keySerializer = new StringRedisSerializer();
    
    public static byte[] buildClientKey(String key) {
        String _key = Constants.CLIENT_KEY+key;
        return keySerializer.serialize(_key);
    }
    public static byte[] buildUserKey(String key) {
        String _key = Constants.USER_KEY+key;
        return keySerializer.serialize(_key);
    }

    public static byte[] buildFreshTokenKey(String key) {
        String _key = Constants.FRESH_TOKEN_KEY+key;
        return keySerializer.serialize(_key);
    }

    /**
     * 存 OAuth2AccessToken
     * @param key
     * @return
     */
    public static byte[] buildAccessTokenKey(String key) {
        String _key = Constants.ACCESS_TOKEN_KEY+key;
        return keySerializer.serialize(_key);
    }

    public static byte[] buildAuthenticationKey(String key) {
        String _key = Constants.AUTHENTICATION_KEY+key;
        return keySerializer.serialize(_key);
    }
}
