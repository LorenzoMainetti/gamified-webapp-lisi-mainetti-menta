package it.polimi.db2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RewardKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "product_id")
    private int productId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RewardKey rewardKey = (RewardKey) o;
        return productId == rewardKey.productId &&
                Objects.equals(userId, rewardKey.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }

}
