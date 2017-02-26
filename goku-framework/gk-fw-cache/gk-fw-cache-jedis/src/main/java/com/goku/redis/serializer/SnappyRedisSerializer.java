package com.goku.redis.serializer;

import lombok.extern.slf4j.Slf4j;

import org.apache.log4j.Logger;
import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * SnappyRedisSerializer
 * @author moueimei
 */
@Slf4j
public class SnappyRedisSerializer<T> implements RedisSerializer<T> {
	private static Logger log = Logger.getLogger(SnappyRedisSerializer.class);
    private final RedisSerializer<T> inner;

    public SnappyRedisSerializer() {
        this(new FstRedisSerializer<T>());
    }

    public SnappyRedisSerializer(RedisSerializer<T> innerSerializer) {
        assert (innerSerializer != null);
        this.inner = innerSerializer;
    }

    @Override
    public byte[] serialize(T graph) {
        try {
            return Snappy.compress(inner.serialize(graph));
        } catch (IOException e) {
            log.error("Fail to serialize graph.", e);
            return EMPTY_BYTES;
        }
    }

    @Override
    public T deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return null;
        try {
            return inner.deserialize(Snappy.uncompress(bytes));
        } catch (IOException e) {
            log.error("Fail to deserialize graph.", e);
            return null;
        }
    }
}
