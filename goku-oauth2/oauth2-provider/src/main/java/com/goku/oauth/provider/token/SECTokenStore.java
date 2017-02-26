package com.goku.oauth.provider.token;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.goku.oauth.Constants;
import com.goku.oauth.model.GkAccessToken;
import com.goku.oauth.model.GkRefreshToken;
import com.goku.oauth.oauth.SECClientDetailsService;
import com.goku.oauth.service.GkAccessTokenService;
import com.goku.oauth.service.GkRefreshTokenService;
import com.goku.oauth.userdetails.GkUserDetails;
import com.goku.oauth.util.ExtractUtils;
import com.goku.oauth.util.OauthSerializationUtils;
import com.goku.oauth.util.OauthTokenKeyUtils;
import com.goku.redis.serializer.SnappyRedisSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public class SECTokenStore implements TokenStore {

    private static final Log LOG = LogFactory.getLog(SECTokenStore.class);
    private int accessTokenValiditySeconds = Constants.ACCESS_TOKEN_VALIDITY_SECONDS_DEFAULT; // default 12 hours.
    @Autowired
    private GkAccessTokenService gkAccessTokenService;
    @Autowired
    private GkRefreshTokenService gkRefreshTokenService;
    @Autowired
    private AuthenticationKeyGenerator authenticationKeyGenerator;

    @Autowired
    private SECClientDetailsService secClientDetailsService;

    @Autowired
    private Jedis jedis;


    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        GkAccessToken  gkAccessToken = null;
        try {
            gkAccessToken = gkAccessTokenService.queryAuthenticationByTokenId(ExtractUtils.extractTokenKey(token));
            if( gkAccessToken !=null &&  gkAccessToken.getAuthentication() != null ){
                return  OauthSerializationUtils.deserializeAuthentication(gkAccessToken.getAuthentication());
            }
            return null;
        } catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + token);
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize authentication for " + token);
            if( gkAccessToken!=null )
                removeAccessToken(gkAccessToken.getId());
        }
        return null;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken oAuth2AccessToken = null;
        try {

            oAuth2AccessToken = getAccessTokenFromCacheByTokenValue(tokenValue);
            return oAuth2AccessToken;
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + tokenValue);
            }
        }
        catch (IllegalArgumentException e) {
            if( oAuth2AccessToken!=null )
                removeAccessToken(oAuth2AccessToken.getValue());
        }
        return null;
    }

    private OAuth2AccessToken getAccessTokenFromCacheByTokenValue(String tokenValue) {
        if(StringUtils.isEmpty(tokenValue)){
            return null;
        }
        byte[] _key = OauthTokenKeyUtils.buildAccessTokenKey(tokenValue);
        byte[] values = jedis.get(_key);
        if( values !=null && values.length > 0){
            SnappyRedisSerializer<OAuth2AccessToken> serializer = new SnappyRedisSerializer<OAuth2AccessToken>();
            return serializer.deserialize(values);
        }
        GkAccessToken gkAccessToken = gkAccessTokenService.queryTokenByTokenId(ExtractUtils.extractTokenKey(tokenValue));
        return OauthSerializationUtils.deserializeAccessToken(gkAccessToken);
    }


    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
//        String refreshToken = null;
//        if (token.getRefreshToken() != null) {
//            refreshToken = token.getRefreshToken().getValue();
//        }
//        byte[] tokenBytes = OauthSerializationUtils.serializeAccessToken(token);
//        String tokenId = ExtractUtils.extractTokenKey(token.getValue());
//        byte[] authenticationBytes = OauthSerializationUtils.serializeAuthentication(authentication);
//        int id = gkAccessTokenService.create(tokenId,
//                tokenBytes, authenticationKeyGenerator.extractKey(authentication),
//                authentication.isClientOnly() ? null : authentication.getName(),
//                authentication.getAuthorizationRequest().getClientId(), authenticationBytes
//                , ExtractUtils.extractTokenKey(refreshToken));
//        String userId = EncryptUtil.decrytUserId(token.getValue());
//        if( id > 0 && StringUtils.isNotEmpty(userId)){
//            GkAccessToken  gkAccessToken= new GkAccessToken();
//            gkAccessToken.setId(id);
//            gkAccessToken.setTokenId(tokenId);
//            gkAccessToken.setAuthentication(authenticationBytes);
//            gkAccessToken.setToken(tokenBytes);
//            SnappyRedisSerializer<GkAccessToken> snappyRedisSerializer = new SnappyRedisSerializer<GkAccessToken>();
//            byte[] key = OauthTokenKeyUtils.buildAuthenticationKey(userId);
//            jedis.set(key,snappyRedisSerializer.serialize(gkAccessToken));
//            jedis.expire(key,getAccessTokenValiditySeconds(authentication.getAuthorizationRequest()));
//        }
    }

    public void removeAccessToken(Integer id) {
        if( id != null ) {
            try {
                gkAccessTokenService.deleteByPkId(id);
            }catch (Exception ignore){
                ignore.printStackTrace();
            }
        }else {
            throw new IllegalArgumentException("id不能为空！");
        }
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        removeAccessToken(token.getValue());
    }

    public void removeAccessToken(String tokenValue) {
        gkAccessTokenService.deleteByTokenId( ExtractUtils.extractTokenKey(tokenValue) );
        jedis.del(OauthTokenKeyUtils.buildAccessTokenKey(tokenValue));
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        gkRefreshTokenService.create(ExtractUtils.extractTokenKey(refreshToken.getValue()),
                OauthSerializationUtils.serializeRefreshToken(refreshToken),
                OauthSerializationUtils.serializeAuthentication(authentication));
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String token) {
        GkRefreshToken gkRefreshToken = null;

        try {
            gkRefreshToken = gkRefreshTokenService.queryTokenByRefreshTokenId( ExtractUtils.extractTokenKey(token));
            return OauthSerializationUtils.deserializeRefreshToken(gkRefreshToken);
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find refresh token for token " + token);
            }
        }
        catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize refresh token for token " + token);
            if(gkRefreshToken != null )
                removeRefreshToken(gkRefreshToken.getId());
        }

        return null;
    }



    public void removeRefreshToken(Integer id) {
        gkRefreshTokenService.deleteByPkId(id);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String value) {
        GkRefreshToken gkRefreshToken = null;

        try {
            gkRefreshToken = gkRefreshTokenService.queryAuthenticationByRefreshTokenId( ExtractUtils.extractTokenKey(value));
            return OauthSerializationUtils.deserializeAuthentication(gkRefreshToken);
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + value);
            }
        }
        catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize access token for " + value);
            if( gkRefreshToken != null ){
                removeRefreshToken(gkRefreshToken.getId());
            }
        }

        return null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        removeRefreshToken(token.getValue());
    }
    public void removeRefreshToken(String token) {
        if(StringUtils.isEmpty(token)){
            throw new IllegalArgumentException("tokenId不能为空!");
        }
        gkRefreshTokenService.deleteByRefreshTokenId(ExtractUtils.extractTokenKey(token));
        jedis.del(OauthTokenKeyUtils.buildFreshTokenKey(token));
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        gkAccessTokenService.deleteByRefreshTokenId(ExtractUtils.extractTokenKey(refreshToken));
        jedis.del(OauthTokenKeyUtils.buildFreshTokenKey(refreshToken));
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;
        GkAccessToken gkAccessToken = null;
        String key = authenticationKeyGenerator.extractKey(authentication);
        try {
            //"select token_id, token from oauth_access_token where authentication_id = ?";
            gkAccessToken = queryTokenByAuthenticationId(key,authentication);
            if(gkAccessToken!=null) {
                accessToken = OauthSerializationUtils.deserializeAccessToken(gkAccessToken);
            }
        } catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.debug("Failed to find access token for authentication " + authentication);
            }
        }
        catch (IllegalArgumentException e) {
            LOG.error("Could not extract access token for authentication " + authentication);
        }

        if (accessToken != null
                && !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
            removeAccessToken(gkAccessToken.getId());
            storeAccessToken(accessToken, authentication);
        }
        return accessToken;
    }

//    private void storeAccessToken2Cache(OAuth2AccessToken oAuth2AccessToken,OAuth2Authentication authentication) {
//        if( null == oAuth2AccessToken ){
//            return;
//        }
//        byte[] _key= OauthTokenKeyUtils.buildOAuth2AccessTokenKey(oAuth2AccessToken.getValue());
//        boolean exists = jedis.exists(_key);
//        if( !exists ){
//            SnappyRedisSerializer<OAuth2AccessToken> serializer = new SnappyRedisSerializer<OAuth2AccessToken>();
//                jedis.set(_key,serializer.serialize(oAuth2AccessToken));
//                jedis.expire(_key,getRefreshTokenValiditySeconds(authentication.getAuthorizationRequest()));
//        }
//    }

    private GkAccessToken queryTokenByAuthenticationId(String key, OAuth2Authentication authentication) {
        GkUserDetails gkUserDetails = getAcUserDetails(authentication);
        byte[] _key= OauthTokenKeyUtils.buildAuthenticationKey(gkUserDetails.getUserId()+"");

        byte[] values = jedis.get(_key);
        SnappyRedisSerializer<GkAccessToken> serializer = new SnappyRedisSerializer<GkAccessToken>();
        if( null == values){
            GkAccessToken gkAccessToken = gkAccessTokenService.queryTokenByAuthenticationId(key);
            if( null != gkAccessToken ){
                jedis.set(_key,serializer.serialize(gkAccessToken));
                jedis.expire(_key,getAccessTokenValiditySeconds(authentication.getAuthorizationRequest()));
            }
            return gkAccessToken;
        }

        return serializer.deserialize(values);

    }

    private GkUserDetails getAcUserDetails(OAuth2Authentication authentication) {
        if( null == authentication || authentication.getUserAuthentication() == null){
            return null;
        }
        return (GkUserDetails)authentication.getUserAuthentication().getPrincipal();
    }

    protected int getAccessTokenValiditySeconds(AuthorizationRequest authorizationRequest) {
        if (secClientDetailsService != null) {
            ClientDetails client = secClientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return accessTokenValiditySeconds;
    }

//    /**
//     * The access token validity period in seconds
//     * @param authorizationRequest the current authorization request
//     * @return the access token validity period in seconds
//     */
//    private int getRefreshTokenValiditySeconds(AuthorizationRequest authorizationRequest) {
//        if (secClientDetailsService != null) {
//            ClientDetails client = secClientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
//            Integer validity = client.getRefreshTokenValiditySeconds();
//            if (validity != null) {
//                return validity;
//            }
//        }
//        return refreshTokenValiditySeconds;
//    }

    private final class SafeAccessTokenRowMapper {
        public OAuth2AccessToken mapRow(GkAccessToken gkAccessToken) {
            try {
                return OauthSerializationUtils.deserializeAccessToken(gkAccessToken);
            }
            catch (IllegalArgumentException e) {
                String token = gkAccessToken.getTokenId();
                gkAccessTokenService.deleteByTokenId(token);
                return null;
            }
        }
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByUserName(String userName) {
        List<OAuth2AccessToken> accessTokens = null;

        try {
            //select token_id, token from oauth_access_token where user_name = ?
            List<GkAccessToken>  gkAccessTokenList = gkAccessTokenService.queryTokenByUserName(userName);
            accessTokens = removeNulls(gkAccessTokenList);
//
//            accessTokens = jdbcTemplate.query(selectAccessTokensFromUserNameSql, new SafeAccessTokenRowMapper(),
//                    userName);
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for userName " + userName);
            }
        }

        return accessTokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AccessToken> accessTokens = null;

        try {
            List<GkAccessToken>  gkAccessTokenList = gkAccessTokenService.queryTokenByClientId(clientId);
            accessTokens = removeNulls(gkAccessTokenList);
//            accessTokens = jdbcTemplate.query(selectAccessTokensFromClientIdSql, new SafeAccessTokenRowMapper(),
//                    clientId);
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for clientId " + clientId);
            }
        }
//        accessTokens = removeNulls(accessTokens);

        return accessTokens;
    }

    private List<OAuth2AccessToken> removeNulls(List<GkAccessToken> gkAccessTokenList) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
        if(CollectionUtils.isNotEmpty(gkAccessTokenList)){
            SafeAccessTokenRowMapper safeAccessTokenRowMapper = new SafeAccessTokenRowMapper();
            for (GkAccessToken gkAccessToken:gkAccessTokenList ){
                OAuth2AccessToken auth2AccessToken = safeAccessTokenRowMapper.mapRow(gkAccessToken);
                if( auth2AccessToken != null ) {
                    accessTokens.add(auth2AccessToken);
                }
            }
        }else {
            throw new EmptyResultDataAccessException("Failed to find access token for userName ",-107);
        }
        return accessTokens;
    }


}
