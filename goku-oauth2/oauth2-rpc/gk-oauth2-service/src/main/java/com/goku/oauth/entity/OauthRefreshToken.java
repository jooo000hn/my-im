package com.goku.oauth.entity;

import java.io.Serializable;
import java.util.Date;

public class OauthRefreshToken implements Serializable {
    private static final long serialVersionUID = 858521672355867689L;

    private Integer id;

    private Date createTime;

    private String tokenId;

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

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}