package com.goku.oauth.oauth;

import com.goku.oauth.Constants;
import com.goku.oauth.model.GkClientDetails;
import com.goku.oauth.service.GkClientDetailsService;
import com.goku.oauth.util.OauthTokenKeyUtils;
import com.goku.redis.serializer.SnappyRedisSerializer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.*;
import redis.clients.jedis.Jedis;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
public class SECClientDetailsService implements ClientDetailsService {
    private  static  int refreshClientSeconds = Constants.FRESH_CLIENT_SECONDS;

    @Autowired
    protected GkClientDetailsService gkClientDetailsService;

    private final SnappyRedisSerializer<BaseClientDetails> serializer = new SnappyRedisSerializer<BaseClientDetails>();

    @Autowired
    private Jedis jedis;

    /**
     * Load a client by the client id. This method must not return null.
     *
     * @param clientId The client id.
     * @return The client details.
     * @throws ClientRegistrationException If the client account is locked, expired, disabled, or for any other reason.
     */
    public ClientDetails loadClientByClientId(String clientId)
            throws InvalidClientException {
        if(StringUtils.isEmpty(clientId)){
            throw new InvalidClientException("clientId error");
        }
        BaseClientDetails details = getCache(clientId);
        if( details != null ){
            return details;
        }
        try {
            GkClientDetails gkClientDetails = gkClientDetailsService.loadClientByClientId(clientId);
            details = new ClientDetailsMapper(gkClientDetails).mapper();
        }catch (EmptyResultDataAccessException e) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        setCache(clientId,details);
        return details;
    }

    private void setCache(String clientId, BaseClientDetails details) {
        byte[] key= OauthTokenKeyUtils.buildClientKey(clientId);
        jedis.set(key,serializer.serialize(details));
        jedis.expire(key,refreshClientSeconds);
    }


    private BaseClientDetails getCache(String clientId) {
        return serializer.deserialize(jedis.get(OauthTokenKeyUtils.buildClientKey(clientId)));
    }
}
