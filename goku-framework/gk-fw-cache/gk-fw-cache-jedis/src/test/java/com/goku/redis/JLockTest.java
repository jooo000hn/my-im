package com.goku.redis;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.concurrent.*;

/**
 * Created on 2014/9/27.
 *
 * @author moueimei
 */
public class JLockTest extends AbstractTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(JLockTest.class);

    @Autowired
    private Jedis jedis;

    @Before
    public void setUp() throws Exception {
        jedis.del("redis:lock:test");
    }

    @Test
    public void testJedisProxy(){
//        for(int i=0;i<100;i++) {
//            JLock.getLock("aaaaa");
//            JLock.releaseLock("aaaaa");
//        }
        jedis.del("redis:lock:test");
    }

    @Test
    public void testGetLock() throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(exec);
        for (int n = 0; n < 20; n++) {
            completionService.submit(new TestThread(0));
        }
        int i = 0;
        for (int n = 0; n < 20; n++) {
            i += completionService.take().get();
        }
        exec.shutdown();
        Assert.assertEquals(20, i);
    }

    @Test
    public void testGetLock1() throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(exec);
        for (int n = 0; n < 100; n++) {
            completionService.submit(new TestThread(500));
        }
        int i = 0;
        for (int n = 0; n < 100; n++) {
            i += completionService.take().get();
        }
        exec.shutdown();
        Assert.assertEquals(1, i);
    }

    class TestThread implements Callable<Integer> {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        private int i = 0;

        public TestThread(int i) {
            this.i = i;
        }

        @Override
        public Integer call() throws Exception {
            int m = 0;
            try {
                Thread.sleep(100);
                logger.debug("start");
                if (!JLock.getLock("test", i)) {
                    logger.debug("timeout");
                    m = 0;
                } else {
                    logger.debug("get lock");
                    Thread.sleep(1000);
                    JLock.releaseLock("test");
                    m = 1;
                }
            } catch (Exception e) {
                logger.error("An unexpected error occurred.", e);
            }
            return m;
        }
    }
}
