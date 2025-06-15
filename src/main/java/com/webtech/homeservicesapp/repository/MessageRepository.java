package com.webtech.homeservicesapp.repository;

import com.webtech.homeservicesapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    List<Message> findBySenderIdOrderByCreatedAtDesc(Long senderId);
    List<Message> findByReceiverIdAndStatusOrderByCreatedAtDesc(Long receiverId, com.webtech.homeservicesapp.model.MessageStatus status);
    List<Message> findBySenderIdAndReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);
    List<Message> findByReceiverIdAndSenderIdOrderByCreatedAtDesc(Long receiverId, Long senderId);
    int countByReceiverIdAndStatus(Long receiverId, com.webtech.homeservicesapp.model.MessageStatus status);
}
