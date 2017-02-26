package com.goku.oauth.userdetails.cache;

import com.goku.oauth.Constants;
import com.goku.oauth.util.OauthTokenKeyUtils;
import com.goku.redis.serializer.SnappyRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;


/**
 * User: user
 * Date: 15/11/5
 * Version: 1.0
 */
@Repository("redisBasedUserCache")
public class RedisBasedUserCache implements UserCache {
	
    private  static  int refreshUserSeconds = Constants.FRESH_USER_SECONDS;
    @Autowired
    private Jedis jedis;
    
    private final SnappyRedisSerializer<UserDetails> serializer = new SnappyRedisSerializer<UserDetails>();
    
    @Override
    public UserDetails getUserFromCache(String username) {
      //  return cache.get(username);
        byte[] bytes = jedis.get( OauthTokenKeyUtils.buildUserKey(username) );
        return serializer.deserialize(bytes);
    }
    
    @Override
    public void putUserInCache(UserDetails user) {
       // cache.put(user.getUsername(),user);
        byte[] key = OauthTokenKeyUtils.buildUserKey(user.getUsername());
        jedis.set(key,serializer.serialize(user));
        jedis.expire(key,refreshUserSeconds);
    }
    
    @Override
    public void removeUserFromCache(String username) {
        jedis.del(OauthTokenKeyUtils.buildUserKey(username));
    }
}
