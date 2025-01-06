package com.example.flascash.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendship", schema = "flashcash",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","friend_user_id"})
})
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_user_id", nullable = false)
    private User friendUser;

    private LocalDateTime friendshipDate;

    public Friendship() {}

    public Friendship(User user, User friendUser) {
        this.user = user;
        this.friendUser = friendUser;
        this.friendshipDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(User friendUser) {
        this.friendUser = friendUser;
    }

    public LocalDateTime getFriendshipDate() {
        return friendshipDate;
    }

    public void setFriendshipDate(LocalDateTime friendshipDate) {
        this.friendshipDate = friendshipDate;
    }
}
