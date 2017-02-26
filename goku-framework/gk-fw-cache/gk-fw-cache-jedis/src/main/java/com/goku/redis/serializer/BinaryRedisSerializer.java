/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goku.redis.serializer;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;


/**
 * Serializer for Redis Key or Value
 * @author moueimei
 * @param <T>
 */
@Slf4j
public class BinaryRedisSerializer<T> implements RedisSerializer<T> {
	private static Logger log = Logger.getLogger(BinaryRedisSerializer.class);
    @Override
    public byte[] serialize(final T graph) {
        if (graph == null) return EMPTY_BYTES;
        
        try {
            @Cleanup
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @Cleanup
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(graph);
            oos.flush();

            return os.toByteArray();
        } catch (Exception e) {
            log.warn("Fail to serializer graph. graph=" + graph, e);
            return EMPTY_BYTES;
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(final byte[] bytes) {
        if ( bytes==null || bytes.length == 0 )
            return null;
        try {
            @Cleanup
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            @Cleanup
            ObjectInputStream ois = new ObjectInputStream(is);
            return (T) ois.readObject();
        } catch (Exception e) {
            log.warn("Fail to deserialize bytes.", e);
            return null;
        }
    }
}