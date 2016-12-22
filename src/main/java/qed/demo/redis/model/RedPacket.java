package qed.demo.redis.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lonel on 2016/12/22.
 */
public class RedPacket implements java.io.Serializable {

    private String userId;
    private Integer totalNum;
    private Integer surplusNum;
    private Integer totalMoney;
    private Integer surplusMoney;

    private Map<String, User> userMap = new HashMap();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getSurplusNum() {
        return surplusNum;
    }

    public void setSurplusNum(Integer surplusNum) {
        this.surplusNum = surplusNum;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getSurplusMoney() {
        return surplusMoney;
    }

    public void setSurplusMoney(Integer surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    @Override
    public String toString() {
        return "[" +
                "userId='" + userId + '\'' +
                ", totalNum=" + totalNum +
                ", surplusNum=" + surplusNum +
                ", totalMoney=" + totalMoney +
                ", surplusMoney=" + surplusMoney +
                ", userMap=" + userMap +
                ']';
    }
}
