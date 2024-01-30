package com.inf5190.chat.messages.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.inf5190.chat.messages.model.Message;

import org.springframework.stereotype.Repository;

/**
 * Classe qui gère la persistence des messages.
 * <p>
 * En mémoire pour le moment.
 */
@Repository
public class MessageRepository {
    private final List<Message> messages = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public List<Message> getMessages(Optional<Long> fromId) {
        long id = -1;
        if (fromId.isPresent()) {
            id = fromId.get();
        }
        long finalId = id;
        return messages.stream().filter(message -> message.id() > finalId).collect(Collectors.toList());
    }

    public void createMessage(Message message) {
        Message msg1 = new Message(idGenerator.getAndIncrement(), message.username(), message.timestamp(), message.text());
        messages.add(msg1);
    }

}
