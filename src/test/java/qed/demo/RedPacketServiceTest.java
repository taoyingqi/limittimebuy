package qed.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;
import qed.demo.redis.model.RedPacket;
import qed.demo.redis.model.User;
import qed.demo.service.RedPacketService;
import qed.demo.util.TimeUtil;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lonel on 2016/12/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedPacketServiceTest {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedPacketService redPacketService;

    private String key = "redPacket-aaaa";

    @Before
    public void init() {
        redisTemplate.delete(key);
        ValueOperations<String, RedPacket> strOps = redisTemplate.opsForValue();
        RedPacket redPacket = new RedPacket();
        redPacket.setUserId("0001");
        redPacket.setTotalNum(20);
        redPacket.setTotalMoney(1000);
        redPacket.setSurplusNum(20);
        redPacket.setSurplusMoney(1000);
        strOps.set(key, redPacket);
    }

    @After
    public void after() {
        ValueOperations<String, RedPacket> strOps = redisTemplate.opsForValue();
        RedPacket redPacket = strOps.get(key);
        log.debug(redPacket.toString());
    }

    @Test
    public void test1() {
        User user = new User();
        user.setUserId("0001");
        user.setUn("hello");
        user.setPhoto("hello");
        user.setOpenTime(TimeUtil.nowTimestamp());
        Object obj = redPacketService.cas(key, user);
        if (obj instanceof Collection) {
            log.error(obj.toString());
        } else if (!ObjectUtils.isEmpty(obj)) {
            Integer flag = Integer.parseInt(obj + "");
            if (flag.equals(1)) {
                log.debug("已领过");
            } else if (flag.equals(2)) {
                log.debug("已领完");
            }
        }

        obj = redPacketService.cas(key, user);
        if (obj instanceof Collection) {
            log.error(obj.toString());
        } else if (!ObjectUtils.isEmpty(obj)) {
            Integer flag = Integer.parseInt(obj + "");
            if (flag.equals(1)) {
                log.debug("已领过");
            } else if (flag.equals(2)) {
                log.debug("已领完");
            }
        }
    }

    @Test
    public void test2() throws Exception {
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setUserId("id" + i);
            user.setUn("hello" + i);
            Thread t = new Thread(new ExecuteThread(user));
            t.start();
            latch.countDown();
        }
        Thread.currentThread().sleep(3000);
        log.debug("[count={}]", count);
    }

    private CountDownLatch latch = new CountDownLatch(100);

    private Long count = 0L;

    private class ExecuteThread implements Runnable {
        private User user;


        public ExecuteThread(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            Object result = redPacketService.cas(key, user);
            log.debug("[result={}]", result);
            if (ObjectUtils.isEmpty(result)) {
                synchronized (count) {
                    count++;
                }
            }
        }
    }

}
