package it.polimi.db2.entities;

import it.polimi.db2.entities.ids.RewardKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "reward", schema = "db_gamified_app")
@NamedQuery(name = "Reward.getLeaderboard", query = "SELECT r FROM Reward r  WHERE r.product = ?1 ORDER BY r.points DESC")
@NamedQuery(name = "Reward.getReward", query = "SELECT r FROM Reward r  WHERE r.id = ?1")
@NamedQuery(name = "Reward.deleteQuestionnaire", query = "DELETE FROM Reward r WHERE r.product = ?1")
public class Reward implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RewardKey id;

    @NotNull
    private int points;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    public void setId(RewardKey id) {
        this.id = id;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public RewardKey getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }
}
