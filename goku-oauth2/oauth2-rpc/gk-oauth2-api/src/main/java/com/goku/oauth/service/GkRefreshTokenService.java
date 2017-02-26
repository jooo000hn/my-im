package com.goku.oauth.service;

import com.goku.oauth.model.GkRefreshToken;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public interface GkRefreshTokenService {

    public boolean create(String tokenId, byte[] token, byte[] authentication);

    /**
     * 通过refreshtokenId获取
     * @param tokenId
     * @return
     */
    public GkRefreshToken queryTokenByRefreshTokenId(String tokenId);

    /**
     *
     * @param tokenId
     * @return
     */
    public GkRefreshToken queryAuthenticationByRefreshTokenId(String tokenId);


    /**
     * 删除AcRefreshToken
     * @param id
     */
    public void deleteByPkId(Integer id);

    /**
     *
     * @param tokenId
     * @return
     */
    public  boolean deleteByRefreshTokenId(String tokenId);
}
