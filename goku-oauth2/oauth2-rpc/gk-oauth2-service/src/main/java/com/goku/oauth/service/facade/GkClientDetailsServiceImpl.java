package com.goku.oauth.service.facade;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.goku.oauth.entity.OauthClientDetails;
import com.goku.oauth.model.GkClientDetails;
import com.goku.oauth.service.GkClientDetailsService;
import com.goku.oauth.service.OauthClientDetailsService;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
@Service("gkClientDetailsService")
public class GkClientDetailsServiceImpl implements GkClientDetailsService {
    @Autowired
    OauthClientDetailsService oauthClientDetailsService;
    @Override
    public GkClientDetails loadClientByClientId(String clientId) {
        if(StringUtils.isBlank(clientId)){
           return null;
        }
        OauthClientDetails oauthClientDetails = oauthClientDetailsService.selectByPrimaryKey(clientId);
        GkClientDetails acClientDetails = new GkClientDetails();
        BeanUtils.copyProperties(oauthClientDetails,acClientDetails);
        return acClientDetails;
    }
}
