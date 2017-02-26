package com.goku.user.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 提供给Oauth认证体系使用
 * User: moueiei
 * Date: 15/11/5
 * Version: 1.0
 */
public class OauthUser implements Serializable {
    /**
     * 用户id
     */
    private int id;
    
    /**
     * 平台userid
     */
    private Integer puId;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 盐值
     */
    private String pwdSalt;

    /**
     * 姓名
     */
    private String username;

    /**
     * 有效截止日期-ExpireDate
     */
    private Date effectdateend;

    /**
     * 账号是否被锁定
     */
    private boolean accountlocked;

    /**
     * 是否可用
     */
    private boolean enabled;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public Integer getPuId() {
        return puId;
    }

    public void setPuId(Integer puId) {
        this.puId = puId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPwdSalt() {
        return pwdSalt;
    }

    public void setPwdSalt(String pwdSalt) {
        this.pwdSalt = pwdSalt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getEffectdateend() {
        return effectdateend;
    }

    public void setEffectdateend(Date effectdateend) {
        this.effectdateend = effectdateend;
    }

    public boolean isAccountlocked() {
        return accountlocked;
    }

    public void setAccountlocked(boolean accountlocked) {
        this.accountlocked = accountlocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OauthUser oauthUser = (OauthUser) o;
        return Objects.equals(id, oauthUser.id) &&
                Objects.equals(accountlocked, oauthUser.accountlocked) &&
                Objects.equals(enabled, oauthUser.enabled) &&
                Objects.equals(password, oauthUser.password) &&
                Objects.equals(pwdSalt, oauthUser.pwdSalt) &&
                Objects.equals(username, oauthUser.username) &&
                Objects.equals(effectdateend, oauthUser.effectdateend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, pwdSalt, username, effectdateend, accountlocked, enabled);
    }
}
