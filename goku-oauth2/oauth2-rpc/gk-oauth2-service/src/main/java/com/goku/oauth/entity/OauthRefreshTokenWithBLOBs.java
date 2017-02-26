package com.goku.oauth.entity;

import java.io.Serializable;

public class OauthRefreshTokenWithBLOBs extends OauthRefreshToken implements Serializable {
    private static final long serialVersionUID = 922331446628404295L;

    private byte[] token;

    private byte[] authentication;

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