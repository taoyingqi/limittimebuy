package qed.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by lonel on 2016/12/21.
 */
@Service
public class LimitBuyService {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Object cas(String key, Integer value) throws DataAccessException {
        return redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);
                Integer origin = Integer.parseInt(operations.opsForValue().get(key) + "");
                if (origin - value > 0) {
                    operations.multi();
                    operations.opsForValue().set(key, (origin - value) + "");
                    return operations.exec();
                }
                return 1;
            }
        });
    }


}
