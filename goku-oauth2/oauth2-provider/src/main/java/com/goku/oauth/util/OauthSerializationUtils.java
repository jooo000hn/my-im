package com.goku.oauth.util;

import com.goku.oauth.model.GkAccessToken;
import com.goku.oauth.model.GkRefreshToken;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * User: user
 * Date: 15/11/26
 * Version: 1.0
 */
public class OauthSerializationUtils {

    /**
     * 序列化refreshToken
     *
     * @param token
     * @return
     */
    public static byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token);
    }

    /**
     * 反序列化 OAuth2AccessToken
     *
     * @param gkAccessToken
     * @return
     */
    public static OAuth2AccessToken deserializeAccessToken(GkAccessToken gkAccessToken) {
        if (null == gkAccessToken) {
            throw new EmptyResultDataAccessException("未查到gkAccessToken", -104);
        }
        return SerializationUtils.deserialize(gkAccessToken.getToken());
    }

    /**
     * 反序列化 OAuth2AccessToken
     *
     * @param token
     * @return
     */
    public static OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    /**
     * 序列化OAuth2AccessToken
     *
     * @param token
     * @return
     */
    public static byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    public static byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    /**
     * 反序列化OAuth2RefreshToken
     *
     * @param acRefreshToken
     * @return
     */
    public static OAuth2RefreshToken deserializeRefreshToken(GkRefreshToken acRefreshToken) {
        if (acRefreshToken == null) {
            throw new EmptyResultDataAccessException("GkRefreshToken数据未查到", -103);
        }
        return SerializationUtils.deserialize(acRefreshToken.getToken());
    }

    /***
     * 反序列化OAuth2Authentication
     *
     * @param acRefreshToken
     * @return
     */
    public static OAuth2Authentication deserializeAuthentication(GkRefreshToken acRefreshToken) {
        if (acRefreshToken == null) {
            throw new EmptyResultDataAccessException("无访问acRefreshToken信息", -101);
        }
        return deserializeAuthentication(acRefreshToken.getAuthentication());
    }

    /**
     * 反序列化OAuth2Authentication
     *
     * @param authentication
     * @return
     */
    public static OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        if (authentication == null) {
            throw new EmptyResultDataAccessException("无访问Token信息", -101);
        }
        return SerializationUtils.deserialize(authentication);
    }
}
