package com.inf5190.chat.messages.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.inf5190.chat.auth.repository.FirestoreUserAccount;
import com.inf5190.chat.auth.repository.UserAccountRepository;
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
    private final Firestore firestore = FirestoreClient.getFirestore();
    private static final String COLLECTION_NAME = "messages";
    public MessageRepository() {
    }

    public List<Message> getMessages(Optional<Long> fromId) {
        long id = -1;
        if (fromId.isPresent()) {
            id = fromId.get();
        }
        long finalId = id;
        return messages.stream().filter(message -> message.id() > finalId).collect(Collectors.toList());
    }

    public Message createMessage(Message message) throws ExecutionException, InterruptedException {
//        Message msg1 = new Message(idGenerator.getAndIncrement(), message.username(), message.timestamp(), message.text());
//        messages.add(msg1);
        Timestamp timestamp = Timestamp.of(new java.sql.Timestamp(message.timestamp()));
        FirestoreMessage firestoreMessage = new FirestoreMessage(message.username(), timestamp, message.text());
        final CollectionReference collectionRef = firestore.collection(COLLECTION_NAME);
        final DocumentReference docRef = collectionRef.document();
        final ApiFuture<WriteResult> apiFuture = docRef.create(firestoreMessage);
        WriteResult writeResult = apiFuture.get();
        return new Message(docRef.getId(),
                message.username(),
                writeResult.getUpdateTime().toDate().getTime(),
                message.text());
    }

}
