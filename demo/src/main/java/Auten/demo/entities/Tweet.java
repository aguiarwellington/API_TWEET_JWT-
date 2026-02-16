package Auten.demo.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity

@Table(name = "tb_tweets")

public class Tweet {
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    @Column (name = "tweet_id")
    private long tweetId;

    private User user;

    private String content;

    @CreationTimestamp
    private String creationTimestamp;

}
