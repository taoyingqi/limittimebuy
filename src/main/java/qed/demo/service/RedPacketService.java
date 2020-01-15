package qed.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import qed.demo.redis.model.RedPacket;
import qed.demo.redis.model.User;
import qed.demo.util.RedPacketUtil;

import java.util.Collection;
import java.util.List;

/**
 * Created by lonel on 2016/12/22.
 */
@Service
public class RedPacketService {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private RedisTemplate redisTemplate;


    public Object cas1(String key, User user) throws DataAccessException {
        return redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);
                RedPacket redPacket = (RedPacket) operations.opsForValue().get(key);
                if (redPacket.getUserMap().containsKey(user.getUserId())) {
                    return 1;
                }
                //拆红包工具
                Integer money = RedPacketUtil.splitRedPacket(redPacket.getSurplusMoney(), redPacket.getSurplusNum());
                if (ObjectUtils.isEmpty(money)) {
                    return 2;
                }
                operations.multi();
                redPacket.setSurplusNum(redPacket.getSurplusNum() - 1);
                redPacket.setSurplusMoney(redPacket.getSurplusMoney() - money);
                //设置用户的领取的金额
                user.setAmount(money);
                //把当前用户加入到已领取集合
                redPacket.getUserMap().put(user.getUserId(), user);
                //更新redis
                operations.opsForValue().set(key, redPacket);
                return operations.exec();
            }
        });
    }

    public Object cas(String key, User user) throws DataAccessException {
        return redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                while(true) {
                    try {
                        operations.watch(key);
                        RedPacket redPacket = (RedPacket) operations.opsForValue().get(key);
                        if (redPacket.getUserMap().containsKey(user.getUserId())) {
                            return 1;
                        }
                        //拆红包工具
                        Integer money = RedPacketUtil.splitRedPacket(redPacket.getSurplusMoney(), redPacket.getSurplusNum());
                        if (ObjectUtils.isEmpty(money)) {
                            return 2;
                        }
                        operations.multi();
                        redPacket.setSurplusNum(redPacket.getSurplusNum() - 1);
                        redPacket.setSurplusMoney(redPacket.getSurplusMoney() - money);
                        //设置用户的领取的金额
                        user.setAmount(money);
                        //把当前用户加入到已领取集合
                        redPacket.getUserMap().put(user.getUserId(), user);
                        //更新redis
                        operations.opsForValue().set(key, redPacket);
                        return operations.exec();
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
//                        operations.discard();
                    }
                }
            }
        });
    }

    /**
     * pipeline : 1，正确使用方式
     */
    public void pipelineSample() {
        final byte[] rawKey = redisTemplate.getKeySerializer().serialize("user_total");
        //pipeline
        RedisCallback<List<Object>> pipelineCallback = new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                connection.incr(rawKey);
                connection.incr(rawKey);
                return connection.closePipeline();
            }

        };

        List<Object> results = (List<Object>) redisTemplate.execute(pipelineCallback);
        for (Object item : results) {
            System.out.println(item.toString());
        }
    }

}
