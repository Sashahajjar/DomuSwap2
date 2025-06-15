package com.domuswap.repository;

import com.domuswap.model.Message;
import com.domuswap.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    List<Message> findBySenderIdOrderByCreatedAtDesc(Long senderId);
    List<Message> findByReceiverIdAndStatusOrderByCreatedAtDesc(Long receiverId, MessageStatus status);
    List<Message> findBySenderIdAndReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);
    List<Message> findByReceiverIdAndSenderIdOrderByCreatedAtDesc(Long receiverId, Long senderId);
    int countByReceiverIdAndStatus(Long receiverId, MessageStatus status);
    List<Message> findBySenderIdAndReceiverIdOrderByCreatedAtAsc(Long senderId, Long receiverId);
    List<Message> findByReceiverIdAndSenderIdOrderByCreatedAtAsc(Long receiverId, Long senderId);
    List<Message> findByReceiverIdAndReadFalseOrderByCreatedAtDesc(Long receiverId);
} 