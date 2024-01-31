package com.inf5190.chat.messages.repository;

import java.util.*;
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
import org.checkerframework.common.value.qual.EnsuresMinLenIf;
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

    public List<Message> getMessages(Optional<String> fromId) throws ExecutionException, InterruptedException {
        List<Message> result = new ArrayList<>();
        final CollectionReference collectionRef = firestore.collection(COLLECTION_NAME);
        Query query = firestore.collection(COLLECTION_NAME).orderBy("timestamp", Query.Direction.ASCENDING).limitToLast(20);

        if (fromId.isPresent()) {
            ApiFuture<DocumentSnapshot> future = collectionRef.document(fromId.get()).get();
            DocumentSnapshot snapshot = future.get();
            query = collectionRef.orderBy("timestamp", Query.Direction.ASCENDING).startAfter(snapshot);
        }

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot docSnaphot : querySnapshot.get().getDocuments()) {
            FirestoreMessage firestoreMsg = docSnaphot.toObject(FirestoreMessage.class);

            assert firestoreMsg != null;
            Message message = new Message(docSnaphot.getId(), firestoreMsg.getUsername(), firestoreMsg.getTimestamp().toDate().getTime(), firestoreMsg.getText());
            result.add(message);
        }
        return result;
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
        return new Message(docRef.getId(), message.username(), writeResult.getUpdateTime().toDate().getTime(), message.text());
    }

}
