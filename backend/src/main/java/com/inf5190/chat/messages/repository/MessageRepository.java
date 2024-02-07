package com.inf5190.chat.messages.repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;

import com.google.cloud.firestore.*;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.inf5190.chat.messages.model.Message;
import com.inf5190.chat.messages.model.MessageRequest;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Classe qui gère la persistence des messages.
 * <p>
 * En mémoire pour le moment.
 */
@Repository
public class MessageRepository {
    private static final String BUCKET_NAME = "chat-project-d5a0e.appspot.com";
    private final List<Message> messages = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final Firestore firestore = FirestoreClient.getFirestore();
    private static final String COLLECTION_NAME = "messages";

//    @Autowired
//    @Qualifier("storageBucketName")
//    private String storageBucketName;

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
            Message message = new Message(docSnaphot.getId(), firestoreMsg.getUsername(), firestoreMsg.getTimestamp().toDate().getTime(), firestoreMsg.getText(), firestoreMsg.getImageUrl());
            result.add(message);
        }
        return result;
    }

    public Message createMessage(MessageRequest message) throws ExecutionException, InterruptedException {
//        Message msg1 = new Message(idGenerator.getAndIncrement(), message.text(), message.timestamp(), message.textt());
//        messages.add(msg1);
        String imageUrl = null;
        final CollectionReference collectionRef = firestore.collection(COLLECTION_NAME);
        final DocumentReference docRef = collectionRef.document();
        if (message.imageData() != null) {
            Bucket b = StorageClient.getInstance().bucket(BUCKET_NAME);
            String path = String.format("images/%s.%s", docRef.getId(), message.imageData().type());
            b.create(path, Decoders.BASE64.decode(message.imageData().data()),
                    com.google.cloud.storage.Bucket.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ));
            imageUrl = String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, path);
        }

        FirestoreMessage firestoreMessage = new FirestoreMessage(message.username(), Timestamp.now(), message.text(), imageUrl);
        final ApiFuture<WriteResult> apiFuture = docRef.create(firestoreMessage);
        WriteResult writeResult = apiFuture.get();
        return new Message(docRef.getId(), message.text(), writeResult.getUpdateTime().toDate().getTime(), message.text(), imageUrl);
    }

}
