package com.goku.oauth.model;

import java.io.Serializable;

/**
 * User: user
 * Date: 15/11/8
 * Version: 1.0
 */
public class GkOauthCode implements Serializable {
    private Integer id;
    /**
     * 存储服务端系统生成的code的值(未加密).
     */
    private String code;

    /**
     * 存储将AuthorizationRequestHolder.java对象序列化后的二进制数据.
     */
    private byte[] authentication;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }
}
