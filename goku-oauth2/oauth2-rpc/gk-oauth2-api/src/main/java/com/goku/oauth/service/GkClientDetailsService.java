package com.goku.oauth.service;

import com.goku.oauth.model.GkClientDetails;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public interface GkClientDetailsService {
    /**
     * 根据ClientId获取客户端信息
     * @param clientId
     * @return
     */
    GkClientDetails loadClientByClientId(String clientId);
}
