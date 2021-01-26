package it.polimi.db2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "logs", schema = "db_gamified_app")
public class Log {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "username", insertable = false, updatable = false)
    private User user;

    @NotNull
    private String userId;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Date getDate() {
        return timestamp;
    }

    public void setDate(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
