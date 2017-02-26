package com.goku.oauth.entity;

import java.io.Serializable;
import java.util.Date;

public class OauthCode implements Serializable {
    private static final long serialVersionUID = 193392704763587024L;

    private Integer id;

    private Date createTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 存储服务端系统生成的code的值(未加密).
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code 
	 *            存储服务端系统生成的code的值(未加密).
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return 存储将AuthorizationRequestHolder.java对象序列化后的二进制数据.
     */
    public byte[] getAuthentication() {
        return authentication;
    }

    /**
     * @param authentication 
	 *            存储将AuthorizationRequestHolder.java对象序列化后的二进制数据.
     */
    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }
}