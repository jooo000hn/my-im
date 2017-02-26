package com.goku.oauth.userdetails;

import com.goku.oauth.util.ReflectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * User: user
 * Date: 15/11/5
 * Version: 1.0
 */
public class GkUserDetails extends User {

    private Integer userId;
    /**
     * 盐值
     */
    private String pwdSalt;

    private Map<String, Object> attributes = null;		//用于字段的扩展


    public GkUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public GkUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPwdSalt() {
        return pwdSalt;
    }

    public void setPwdSalt(String pwdSalt) {
        this.pwdSalt = pwdSalt;
    }

    public void putAttribute(String propertyName, Object value) {
        Assert.notNull(propertyName, "propertyName can not be empty.");
        Field field = ReflectionUtils.getAccessibleField(this, propertyName);
        if (field != null) {
            ReflectionUtils.invokeSetterMethod(this, propertyName, value);
        }
        put(propertyName, value);
    }

    public Object getAttribute(String propertyName) {
        Assert.notNull(propertyName, "propertyName can not be empty.");
        Field field = ReflectionUtils.getAccessibleField(this, propertyName);
        if (field != null) {
            return ReflectionUtils.invokeGetterMethod(this, propertyName);
        }
        if (attributes == null) {
            return null;
        }
        return attributes.get(propertyName);
    }

    //是否包含某属性
    public boolean hasAttribute (String propertyName) {
        Field field = ReflectionUtils.getAccessibleField(this, propertyName);
        if (field != null) {
            return true;
        }
        if (attributes != null && attributes.containsKey(propertyName)) {
            return true;
        }
        return false;
    }

    protected void put (String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }
}
