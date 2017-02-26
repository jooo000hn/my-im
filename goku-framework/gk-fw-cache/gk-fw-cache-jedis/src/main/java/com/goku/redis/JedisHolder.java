package com.goku.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Map;

/**
 * Created on 2014/9/26.
 *
 * @author moueimei
 */
public class JedisHolder implements InitializingBean {

    public static final String DEFAULT = "default";

    /**
     * 以空间换时间,实现同步
     */
    private static final NamedThreadLocal<JedisResource<Jedis>> JEDIS_THREAD_LOCAL = new NamedThreadLocal<JedisResource<Jedis>>("jedis resource holder") {
        @Override
        protected JedisResource<Jedis> initialValue() {
            return new JedisResource<>(null);
        }
    };

    private static final NamedThreadLocal<JedisResource<ShardedJedis>> SHARDED_JEDIS_THREAD_LOCAL = new NamedThreadLocal<JedisResource<ShardedJedis>>("shardedJedis resource holder") {
        @Override
        protected JedisResource<ShardedJedis> initialValue() {
            return new JedisResource<>(null);
        }
    };

    private Map<String, JedisPool> jedisPoolMap;

    private ShardedJedisPool shardedJedisPool;

    @Autowired
    private RedisInterceptor redisInterceptor;

    public Jedis get() {
        JedisResource<Jedis> jedisResource = JEDIS_THREAD_LOCAL.get();
        if (jedisResource.hasJedis()) {
            return jedisResource.getJedis();
        }
        return jedisPoolMap.get(DEFAULT).getResource();
    }

    public ShardedJedis getShardedJedis() {
        JedisResource<ShardedJedis> jedisResource = SHARDED_JEDIS_THREAD_LOCAL.get();
        if (jedisResource.hasJedis()) {
            return jedisResource.getJedis();
        } else {
            throw new IllegalArgumentException("no sharded jedis connection.");
        }
    }

    public void release(Jedis jedis, boolean isBroken) {
        if (JEDIS_THREAD_LOCAL.get().hasJedis()) {
            return;
        }
        returnResource(jedis);
        JEDIS_THREAD_LOCAL.remove();
    }

    public boolean hasJedis() {
        return JEDIS_THREAD_LOCAL.get().hasJedis();
    }

    public boolean hasShardJedis() {
        return SHARDED_JEDIS_THREAD_LOCAL.get().hasJedis();
    }

    public void createResource(String val) {
        Jedis jedis;
        JedisResource<Jedis> jedisResource = JEDIS_THREAD_LOCAL.get();
        if (jedisResource.hasJedis()) {
            jedisResource.incrementAndGet();
            return;
        }
        if (val.isEmpty()) {
            jedis = jedisPoolMap.get(DEFAULT).getResource();
        } else {
            jedis = jedisPoolMap.get(val).getResource();
        }
        jedisResource.setJedis(jedis);
    }

    public void createShardedResource() {
        JedisResource<ShardedJedis> jedisResource = SHARDED_JEDIS_THREAD_LOCAL.get();
        if (jedisResource.hasJedis()) {
            jedisResource.incrementAndGet();
            return;
        }
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        jedisResource.setJedis(shardedJedis);
    }

    public void releaseForce() {
        JedisResource<Jedis> jedisResource = JEDIS_THREAD_LOCAL.get();
        if (!jedisResource.hasJedis()) {
            return;
        }
        int c = jedisResource.decrementAndGet();
        if (c == 0) {
            returnResource(jedisResource.getJedis());
            JEDIS_THREAD_LOCAL.remove();
        }
    }

    public void releaseShardedForce(boolean isBroken) {
        JedisResource<ShardedJedis> jedisResource = SHARDED_JEDIS_THREAD_LOCAL.get();
        if (!jedisResource.hasJedis()) {
            return;
        }
        int c = jedisResource.decrementAndGet();
        if (c > 0) {
            return;
        }
        ShardedJedis shardedJedis = jedisResource.getJedis();
        returnResource(shardedJedis);
//        if (isBroken) {
//            shardedJedisPool.returnBrokenResource(shardedJedis);
//        } else {
//            shardedJedisPool.returnResource(shardedJedis);
//        }
        SHARDED_JEDIS_THREAD_LOCAL.remove();
    }
    private void returnResource(ShardedJedis jedis) {
        if( jedis != null ){
            jedis.close();
        }
    }

    private void returnResource(Jedis jedis) {
        if( jedis != null ){
            jedis.close();
        }
    }

    public void setJedisPoolMap(Map<String, JedisPool> jedisPoolMap) {
        this.jedisPoolMap = jedisPoolMap;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public int getConnectionNum(String connName) {
        return jedisPoolMap.getOrDefault(connName, jedisPoolMap.get(DEFAULT)).getNumActive();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisInterceptor.setJedisHolder(this);
    }
}
