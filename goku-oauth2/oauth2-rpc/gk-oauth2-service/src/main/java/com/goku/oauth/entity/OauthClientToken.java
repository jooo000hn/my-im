package com.goku.oauth.entity;

import java.io.Serializable;
import java.util.Date;

public class OauthClientToken implements Serializable {
    private static final long serialVersionUID = 855381177453557614L;

    private Integer id;

    private Date createTime;

    /**
     * 从服务器端获取到的access_token的值.
     */
    private String tokenId;

    /**
     * 该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的. <br>
	 * 具体实现请参考DefaultClientKeyGenerator.java类.
     */
    private String authenticationId;

    /**
     * 登录时的用户名
     */
    private String userName;

    private String clientId;

    /**
     * 这是一个二进制的字段, 存储的数据是OAuth2AccessToken.java对象序列化后的二进制数据.
     */
    private byte[] token;

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
     * @return 从服务器端获取到的access_token的值.
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * @param tokenId 
	 *            从服务器端获取到的access_token的值.
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * @return 该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的. <br>
	 *         具体实现请参考DefaultClientKeyGenerator.java类.
     */
    public String getAuthenticationId() {
        return authenticationId;
    }

    /**
     * @param authenticationId 
	 *            该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的. <br>
	 *            具体实现请参考DefaultClientKeyGenerator.java类.
     */
    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    /**
     * @return 登录时的用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName 
	 *            登录时的用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return 这是一个二进制的字段, 存储的数据是OAuth2AccessToken.java对象序列化后的二进制数据.
     */
    public byte[] getToken() {
        return token;
    }

    /**
     * @param token 
	 *            这是一个二进制的字段, 存储的数据是OAuth2AccessToken.java对象序列化后的二进制数据.
     */
    public void setToken(byte[] token) {
        this.token = token;
    }
}