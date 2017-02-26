package com.goku.oauth.service;

import com.goku.oauth.model.GkAccessToken;

import java.util.List;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public interface GkAccessTokenService {

    /**
     *
     * @param tokenId
     * @return
     */
    public GkAccessToken queryTokenByTokenId(String tokenId);

    /**
     *
     * @param tokenId
     * @return
     */
    public GkAccessToken queryAuthenticationByTokenId(String tokenId);

    /**
     *
     * @param authenticationId
     * @return
     */
    public GkAccessToken queryTokenByAuthenticationId(String authenticationId);

    /**
     *
     * @param userName
     * @return
     */
    public List<GkAccessToken> queryTokenByUserName(String userName);

    /**
     *
     * @param clientId
     * @return
     */
    public List<GkAccessToken> queryTokenByClientId(String clientId);

    /**
     *
     * @param tokenId
     * @return
     */
    public  boolean deleteByTokenId(String tokenId);

    /**
     * token_id, token, authentication_id, user_name, client_id, authentication, refresh_token
     * @return
     */
    public boolean create(String tokenId, byte[] token, String authenticationId, String userName, String clientId, byte[] authentication, String refreshToken);

    /**
     *
     * @param refreshTokenId
     * @return
     */
    public boolean deleteByRefreshTokenId(String refreshTokenId);


    /**
     * 删除Token
     * @param id
     */
    public void deleteByPkId(Integer id);
}
