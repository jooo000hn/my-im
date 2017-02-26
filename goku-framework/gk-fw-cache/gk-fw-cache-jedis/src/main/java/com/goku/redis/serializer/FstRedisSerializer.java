package com.goku.redis.serializer;


import de.ruedigermoeller.serialization.FSTConfiguration;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

/**
 * Serializer using Fast-Serialization
 *
 * @author moueimei
 */
@Slf4j
public class FstRedisSerializer<T> implements RedisSerializer<T> {
	private static Logger log = Logger.getLogger(FstRedisSerializer.class);
    private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    /**
     * Provides access to serialization configuration, to inject custom ClassLoaders
     * among other things.
     * @return serialization configuration.
     */
    public static FSTConfiguration getConf() {
	return conf;
    }

    @Override
    public byte[] serialize(final T graph) {
        if (graph == null)
            return EMPTY_BYTES;

        try {
            @Cleanup
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            FSTObjectOutput oos = conf.getObjectOutput(os);
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
        if (bytes==null||bytes.length==0)
            return null;

        try {
            @Cleanup
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            FSTObjectInput ois = conf.getObjectInput(is);
            return (T) ois.readObject();
        } catch (Exception e) {
            log.warn("Fail to deserialize bytes.", e);
            return null;
        }
    }
}
