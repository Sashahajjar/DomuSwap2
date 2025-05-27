package com.webtech.homeservicesapp.service;

import com.webtech.homeservicesapp.model.Message;
import com.webtech.homeservicesapp.model.User;
import com.webtech.homeservicesapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void sendMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getConversation(User user1, User user2) {
        List<Message> messages1 = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtDesc(user1.getId(), user2.getId());
        List<Message> messages2 = messageRepository.findByReceiverIdAndSenderIdOrderByCreatedAtDesc(user1.getId(), user2.getId());
        messages1.addAll(messages2);
        messages1.sort((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));
        return messages1;
    }
}
