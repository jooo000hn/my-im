package com.goku.oauth.provider.token;

import com.goku.oauth.Constants;
import com.goku.oauth.userdetails.GkUserDetails;
import com.goku.oauth.util.EncryptUtil;
import com.goku.oauth.util.OauthTokenKeyUtils;
import com.goku.oauth.util.TokenCodeGeneratorUtils;
import com.goku.redis.serializer.SnappyRedisSerializer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * User: user
 * Date: 15/11/11
 * Version: 1.0
 */
public class SECTokenServices implements AuthorizationServerTokenServices, ResourceServerTokenServices,
        ConsumerTokenServices, InitializingBean {

    private int refreshTokenValiditySeconds = Constants.REFRESH_TOKEN_VALIDAITY_SECONDS_DEFAULT; // default 30 days.

    private int accessTokenValiditySeconds = Constants.ACCESS_TOKEN_VALIDITY_SECONDS_DEFAULT; // default 12 hours.

    private boolean supportRefreshToken = false;

    private boolean reuseRefreshToken = true;

    private TokenStore tokenStore;

    private ClientDetailsService clientDetailsService;

    private TokenEnhancer accessTokenEnhancer;


    @Autowired
    private Jedis jedis;

    /**
     * Initialize these token services. If no random generator is set, one will be created.
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(tokenStore, "tokenStore must be set");
    }

    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {

        OAuth2AccessToken existingAccessToken = getAccessToken(authentication);
        OAuth2RefreshToken refreshToken = null;
        if (existingAccessToken != null) {
            if (existingAccessToken.isExpired()) {
                if (existingAccessToken.getRefreshToken() != null) {
                    refreshToken = existingAccessToken.getRefreshToken();
                    // The token store could remove the refresh token when the access token is removed, but we want to
                    // be sure...
                    tokenStore.removeRefreshToken(refreshToken);
                }
                tokenStore.removeAccessToken(existingAccessToken);
            } else {
                storeAccessToken(existingAccessToken, authentication);
                return existingAccessToken;
            }
        }

        // Only create a new refresh token if there wasn't an existing one associated with an expired access token.
        // Clients might be holding existing refresh tokens, so we re-use it in the case that the old access token
        // expired.
        if (refreshToken == null) {
            refreshToken = createRefreshToken(authentication);
        } else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
            if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
                refreshToken = createRefreshToken(authentication);
            }
        }

        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        storeAccessToken(accessToken, authentication);
        tokenStore.storeAccessToken(accessToken, authentication);
        if (refreshToken != null) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }
        return accessToken;

    }

    private void storeAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication authentication) {
        if (oAuth2AccessToken != null) {
            setAccessTokenFromCache(oAuth2AccessToken.getValue(), oAuth2AccessToken, this.getAccessTokenValiditySeconds(authentication.getAuthorizationRequest()));
        }
    }

    /**
     * 刷新token
     *
     * @param refreshTokenValue
     * @param request
     * @return
     * @throws AuthenticationException
     */
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, AuthorizationRequest request)
            throws AuthenticationException {

        if (!supportRefreshToken) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }

        OAuth2RefreshToken refreshToken = getAcRefreshTokenFromCache(refreshTokenValue, request);
        if (refreshToken == null) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }

        OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
        String clientId = authentication.getAuthorizationRequest().getClientId();
        if (clientId == null || !clientId.equals(request.getClientId())) {
            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
        }

        // clear out any access tokens already associated with the refresh token.
        tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);

        if (isExpired(refreshToken)) {
            tokenStore.removeRefreshToken(refreshToken);
            throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
        }

        authentication = createRefreshedAuthentication(authentication, request.getScope());

        if (!reuseRefreshToken) {
            tokenStore.removeRefreshToken(refreshToken);
            refreshToken = createRefreshToken(authentication);
        }

        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        if (!reuseRefreshToken) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }
        return accessToken;
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return tokenStore.getAccessToken(authentication);
    }

    /**
     * Create a refreshed authentication.
     *
     * @param authentication The authentication.
     * @param scope          The scope for the refreshed token.
     * @return The refreshed authentication.
     * @throws InvalidScopeException If the scope requested is invalid or wider than the original scope.
     */
    private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, Set<String> scope) {
        OAuth2Authentication narrowed = authentication;
        if (scope != null && !scope.isEmpty()) {
            AuthorizationRequest clientAuth = authentication.getAuthorizationRequest();
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope
                        + ".", originalScope);
            } else {
                narrowed = new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
            }
        }
        return narrowed;
    }

    protected boolean isExpired(OAuth2RefreshToken refreshToken) {
        if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken) refreshToken;
            return expiringToken.getExpiration() == null
                    || System.currentTimeMillis() > expiringToken.getExpiration().getTime();
        }
        return false;
    }

    public OAuth2AccessToken readAccessToken(String accessToken) {
        return tokenStore.readAccessToken(accessToken);
    }

    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException {
        OAuth2AccessToken accessToken = getAccessTokenFromCache(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        }

        OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
        return result;
    }


    public String getClientId(String tokenValue) {
        OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
        if (authentication == null) {
            throw new InvalidTokenException("Invalid access token: " + tokenValue);
        }
        AuthorizationRequest authorizationRequest = authentication.getAuthorizationRequest();
        if (authorizationRequest == null) {
            throw new InvalidTokenException("Invalid access token (no client id): " + tokenValue);
        }
        return authorizationRequest.getClientId();
    }

    public Collection<OAuth2AccessToken> findTokensByUserName(String userName) {
        return tokenStore.findTokensByUserName(userName);
    }

    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return tokenStore.findTokensByClientId(clientId);
    }

    public boolean revokeToken(String tokenValue) {
        if (StringUtils.isEmpty(tokenValue)) {
            return false;
        }
        String userId = EncryptUtil.decrytUserId(tokenValue);
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        jedis.del(OauthTokenKeyUtils.buildAuthenticationKey(userId));
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null) {
            return false;
        }
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
        return true;
    }

    private ExpiringOAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        if (!isSupportRefreshToken(authentication.getAuthorizationRequest())) {
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getAuthorizationRequest());
        GkUserDetails gkUserDetails = (GkUserDetails) authentication.getUserAuthentication().getPrincipal();
        ExpiringOAuth2RefreshToken refreshToken = new DefaultExpiringOAuth2RefreshToken(TokenCodeGeneratorUtils.randomUUID(gkUserDetails),
                new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        return refreshToken;
    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        GkUserDetails gkUserDetails = (GkUserDetails) authentication.getUserAuthentication().getPrincipal();
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(TokenCodeGeneratorUtils.randomUUID(gkUserDetails));
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getAuthorizationRequest());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getAuthorizationRequest().getScope());

        return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
    }

    /**
     * The access token validity period in seconds
     *
     * @param authorizationRequest the current authorization request
     * @return the access token validity period in seconds
     */
    protected int getAccessTokenValiditySeconds(AuthorizationRequest authorizationRequest) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return accessTokenValiditySeconds;
    }

    /**
     * The refresh token validity period in seconds
     *
     * @param authorizationRequest the current authorization request
     * @return the refresh token validity period in seconds
     */
    protected int getRefreshTokenValiditySeconds(AuthorizationRequest authorizationRequest) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
            Integer validity = client.getRefreshTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return refreshTokenValiditySeconds;
    }

    /**
     * Is a refresh token supported for this client (or the global setting if
     * {@link #setClientDetailsService(ClientDetailsService) clientDetailsService} is not set.
     *
     * @param authorizationRequest the current authorization request
     * @return boolean to indicate if refresh token is supported
     */
    protected boolean isSupportRefreshToken(AuthorizationRequest authorizationRequest) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
            return client.getAuthorizedGrantTypes().contains("refresh_token");
        }
        return this.supportRefreshToken;
    }

    /**
     * An access token enhancer that will be applied to a new token before it is saved in the token store.
     *
     * @param accessTokenEnhancer the access token enhancer to set
     */
    public void setTokenEnhancer(TokenEnhancer accessTokenEnhancer) {
        this.accessTokenEnhancer = accessTokenEnhancer;
    }

    /**
     * The validity (in seconds) of the refresh token.
     *
     * @param refreshTokenValiditySeconds The validity (in seconds) of the refresh token.
     */
    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    /**
     * The default validity (in seconds) of the access token. Zero or negative for non-expiring tokens. If a client
     * details service is set the validity period will be read from he client, defaulting to this value if not defined
     * by the client.
     *
     * @param accessTokenValiditySeconds The validity (in seconds) of the access token.
     */
    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    /**
     * Whether to support the refresh token.
     *
     * @param supportRefreshToken Whether to support the refresh token.
     */
    public void setSupportRefreshToken(boolean supportRefreshToken) {
        this.supportRefreshToken = supportRefreshToken;
    }

    /**
     * Whether to reuse refresh tokens (until expired).
     *
     * @param reuseRefreshToken Whether to reuse refresh tokens (until expired).
     */
    public void setReuseRefreshToken(boolean reuseRefreshToken) {
        this.reuseRefreshToken = reuseRefreshToken;
    }

    /**
     * The persistence strategy for token storage.
     *
     * @param tokenStore the store for access and refresh tokens.
     */
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * The client details service to use for looking up clients (if necessary). Optional if the access token expiry is
     * set globally via {@link #setAccessTokenValiditySeconds(int)}.
     *
     * @param clientDetailsService the client details service
     */
    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }


    private void setAcRefreshTokenFromCache(String token, OAuth2RefreshToken auth2RefreshToken, int refreshTokenValiditySeconds) {
        if (auth2RefreshToken == null && StringUtils.isEmpty(token)) {
            return;
        }
        SnappyRedisSerializer<OAuth2RefreshToken> serializer = new SnappyRedisSerializer<OAuth2RefreshToken>();
        byte[] key = OauthTokenKeyUtils.buildFreshTokenKey(token);
        jedis.set(key, serializer.serialize(auth2RefreshToken));
        jedis.expire(key, refreshTokenValiditySeconds);
    }

    private void setAccessTokenFromCache(String token, OAuth2AccessToken oAuth2AccessToken, int accessTokenValiditySeconds) {
        if (oAuth2AccessToken == null && StringUtils.isEmpty(token)) {
            return;
        }
        SnappyRedisSerializer<OAuth2AccessToken> serializer = new SnappyRedisSerializer<OAuth2AccessToken>();
        byte[] key = OauthTokenKeyUtils.buildAccessTokenKey(token);
        jedis.set(key, serializer.serialize(oAuth2AccessToken));
        jedis.expire(key, accessTokenValiditySeconds);
    }

    private OAuth2AccessToken getAccessTokenFromCache(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        OAuth2AccessToken oAuth2AccessToken = null;
        byte[] bytes = jedis.get(OauthTokenKeyUtils.buildAccessTokenKey(token));
        if (bytes != null && bytes.length > 0) {
            SnappyRedisSerializer<OAuth2AccessToken> serializer = new SnappyRedisSerializer<OAuth2AccessToken>();
            oAuth2AccessToken = serializer.deserialize(bytes);
        }
        return oAuth2AccessToken;
    }

    private OAuth2RefreshToken getAcRefreshTokenFromCache(String token, AuthorizationRequest request) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        OAuth2RefreshToken oAuth2RefreshToken = null;
        byte[] bytes = jedis.get(OauthTokenKeyUtils.buildFreshTokenKey(token));
        if (bytes != null && bytes.length > 0) {
            SnappyRedisSerializer<OAuth2RefreshToken> serializer = new SnappyRedisSerializer<OAuth2RefreshToken>();
            oAuth2RefreshToken = serializer.deserialize(bytes);
        }
        if (oAuth2RefreshToken == null) {
            oAuth2RefreshToken = tokenStore.readRefreshToken(token);
            setAcRefreshTokenFromCache(token, oAuth2RefreshToken, this.getRefreshTokenValiditySeconds(request));
        }
        return oAuth2RefreshToken;
    }
}
