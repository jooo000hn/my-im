package com.goku.im.logic.redis;

import com.goku.im.logic.global.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by moueimei on 15/11/26.
 * 数据库待处理消息队列,负责存储用户发送的IM消息,由logic集群负责添加,后端persistence集群读取并存入数据库
 */
@Repository
public class DataBaseMessageQueue {
    @Autowired
    private Jedis jedis;

    public void push(String message) throws Exception {
        String key = RedisKeyConst.DATABASE_MESSAGE_QUEUE_KEY;
        jedis.lpush(key, message);
    }
}