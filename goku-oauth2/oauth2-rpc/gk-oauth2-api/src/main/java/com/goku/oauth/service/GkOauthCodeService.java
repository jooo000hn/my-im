package com.goku.oauth.service;

import com.goku.oauth.model.GkOauthCode;

/**
 * User: user
 * Date: 15/11/8
 * Version: 1.0
 */
public interface GkOauthCodeService {
    /**
     * 生成tokenCode
     * @param code
     * @param authentication
     */
    public void create(String code, byte[] authentication);

    /**
     *
     * @param code
     * @return
     */
    public GkOauthCode queryByCode(String code);

    /**
     *
     * @param id
     */
    public void deleteByPkId(Integer id);
}
