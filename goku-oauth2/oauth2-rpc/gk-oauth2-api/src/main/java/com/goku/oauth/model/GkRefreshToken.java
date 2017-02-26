package com.goku.oauth.model;

import java.io.Serializable;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public class GkRefreshToken implements Serializable {


    private Integer id;

    /**
     * 该字段的值是将refresh_token的值通过MD5加密后存储的.
     */
    private String tokenId;

    /**
     * 存储将OAuth2RefreshToken.java对象序列化后的二进制数据.
     */
    private byte[] token;

    /**
     * 存储将OAuth2Authentication.java对象序列化后的二进制数据.
     */
    private byte[] authentication;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    public byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }
}
