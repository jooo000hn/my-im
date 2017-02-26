package com.goku.oauth.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.TokenServicesUserApprovalHandler;
import com.goku.oauth.model.GkClientDetails;
import com.goku.oauth.service.GkClientDetailsService;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public class SECTokenServicesUserApprovalHandler  extends TokenServicesUserApprovalHandler {

    @Autowired
    protected GkClientDetailsService acClientDetailsService;

    public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        if (super.isApproved(authorizationRequest, userAuthentication)) {
            return true;
        }
        if (!userAuthentication.isAuthenticated()) {
            return false;
        }

        GkClientDetails clientDetails = acClientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
        return clientDetails != null && clientDetails.getTrusted();

    }
}
