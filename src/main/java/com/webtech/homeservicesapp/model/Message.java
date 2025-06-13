package com.webtech.homeservicesapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonProperty("sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonProperty("receiver")
    private User receiver;

    @Column(nullable = false)
    @JsonProperty("content")
    private String content;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("createdAt")
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JsonProperty("status")
    private MessageStatus status;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        if (status == null) {
            status = MessageStatus.PENDING;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}
