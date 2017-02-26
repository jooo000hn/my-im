package com.goku.oauth.oauth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.provider.BaseClientDetails;
import com.goku.oauth.model.GkClientDetails;

import java.util.Map;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public class ClientDetailsMapper {

    private static final Log logger = LogFactory.getLog(ClientDetailsMapper.class);

    GkClientDetails acClientDetails;

    public ClientDetailsMapper(GkClientDetails acClientDetails){
        this.acClientDetails =acClientDetails;
    }

    public BaseClientDetails mapper(){
        if(acClientDetails==null){
            throw new EmptyResultDataAccessException("无客户端注册信息",-100);
        }
        BaseClientDetails details = new BaseClientDetails(acClientDetails.getClientId(), acClientDetails.getResourceIds(),
                acClientDetails.getScope(),acClientDetails.getAuthorizedGrantTypes(),acClientDetails.getAuthorities(),acClientDetails.getWebServerRedirectUri());
        details.setClientSecret(acClientDetails.getClientSecret());
        if ( acClientDetails.getAccessTokenValidity() != null) {
            details.setAccessTokenValiditySeconds(acClientDetails.getAccessTokenValidity());
        }
        if (acClientDetails.getRefreshTokenValidity() != null) {
            details.setRefreshTokenValiditySeconds(acClientDetails.getRefreshTokenValidity());
        }
        String json = acClientDetails.getAdditionalInformation();
        if (json != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> additionalInformation = mapper.readValue(json, Map.class);
                details.setAdditionalInformation(additionalInformation);
            }catch (Exception e) {
                logger.warn("Could not decode JSON for additional information: " + details, e);
            }
        }
        return details;
    }
}
