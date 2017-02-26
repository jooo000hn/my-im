package com.goku.oauth.impl;

import com.goku.oauth.userdetails.GkUserDetails;
import com.goku.oauth.userdetails.cache.RedisBasedUserCache;
import com.goku.user.model.OauthUser;
import com.goku.user.service.GkUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User: user
 * Date: 15/11/5
 * Version: 1.0
 */
@Service("gkUserDetailsService")
public class GkUserDetailsService implements UserDetailsService {
    protected static final GrantedAuthority DEFAULT_USER_ROLE = new SimpleGrantedAuthority("ROLE_USER");

    @Autowired
    GkUserService gkUserService;
    @Autowired
    private RedisBasedUserCache redisBasedUserCache;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(username)){
            throw new IllegalArgumentException("username - 参数错误");
        }
        UserDetails acUserDetails =  redisBasedUserCache.getUserFromCache(username);
        if( null != acUserDetails ){
            return acUserDetails;
        }
       return findAcUserDetails(username);
    }

    private GkUserDetails findAcUserDetails(String username) {
        OauthUser oauthUser =  gkUserService.findOauthUserByName(username);
        if( null == oauthUser ){
            throw new UsernameNotFoundException("userNoExist!");
        }
        //String username, String password, boolean enabled, boolean accountNonExpired,
        // boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities
        boolean accountNonExpired     = oauthUser.getEffectdateend() !=null? oauthUser.getEffectdateend().after(new Date()):true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked    = !oauthUser.isAccountlocked();
        Set<GrantedAuthority> grantedAuthorities = initAuthorities();
        GkUserDetails gkUserDetails = new GkUserDetails(oauthUser.getUsername(),oauthUser.getPassword(),oauthUser.isEnabled(),accountNonExpired,credentialsNonExpired,accountNonLocked,grantedAuthorities);
        gkUserDetails.setUserId(oauthUser.getPuId());
        redisBasedUserCache.putUserInCache(gkUserDetails);
        return gkUserDetails;
    }

    private Set<GrantedAuthority> initAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add( DEFAULT_USER_ROLE);
//        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + cc.wdcy.domain.user.Privilege.UNITY.name()));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MOBILE"));
//        initAuthorities();
        return grantedAuthorities;

    }
}
