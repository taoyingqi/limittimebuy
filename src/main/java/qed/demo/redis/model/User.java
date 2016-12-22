package qed.demo.redis.model;

import java.sql.Timestamp;

/**
 * Created by lonel on 2016/12/21.
 */
public class User implements java.io.Serializable {
    private String userId;
    private String un;
    private String photo;
    private Integer amount;
    private Timestamp openTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Timestamp getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Timestamp openTime) {
        this.openTime = openTime;
    }

    @Override
    public String toString() {
        return "[" +
                "userId='" + userId + '\'' +
                ", un='" + un + '\'' +
                ", photo='" + photo + '\'' +
                ", amount='" + amount + '\'' +
                ", openTime=" + openTime +
                ']';
    }
}
