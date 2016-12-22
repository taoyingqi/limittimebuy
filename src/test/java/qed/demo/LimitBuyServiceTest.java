package qed.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;
import qed.demo.service.LimitBuyService;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lonel on 2016/12/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LimitBuyServiceTest {
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private LimitBuyService limitBuyService;

    private String key = "test-cas-1";

    @Before
    public void init() {
        ValueOperations<String, String> strOps = redisTemplate.opsForValue();
        strOps.set(key, "1000");
    }

    @After
    public void after() {
        redisTemplate.delete(key);
    }


    @Test
    public void test1() {
        Object result = limitBuyService.cas(key, 1);
        if (ObjectUtils.isEmpty(result)) {
            System.out.println("SUCCESS");
        }
    }


    @Test
    public void test2(){
        try {
            for (int i = 0; i < 1000; i++) {
                Thread t = new Thread(new ExecuteThread(13));
                t.start();
                latch.countDown();
            }
            Thread.currentThread().sleep(3000);
            log.debug("[total={}, count={}]", total, count);
        } catch (Exception ex) {
            log.error("buy failure");
        }
    }


    private CountDownLatch latch = new CountDownLatch(100);

    private Long total = 0L;
    private Long count = 0L;

    private class ExecuteThread implements Runnable {
        private Integer amount;

        public ExecuteThread(Integer amount) {
            this.amount = amount;
        }
        @Override
        public void run() {
            Object result = limitBuyService.cas(key, amount);
            if (ObjectUtils.isEmpty(result)) {
                synchronized (total) {
                    total += amount;
                    count++;
                }
            }
        }
    }

}
