package com.inf5190.chat.auth.repository;

import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;
import com.google.firebase.cloud.FirestoreClient;

@Repository
public class UserAccountRepository {
    private static final String COLLECTION_NAME = "userAccounts";
    private final Firestore firestore = FirestoreClient.getFirestore();

    public FirestoreUserAccount getUserAccount(String username) throws
            InterruptedException, ExecutionException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists())
            return document.toObject(FirestoreUserAccount.class);
        return null;
//        throw new UnsupportedOperationException("A faire");
    }

    public void setUserAccount(FirestoreUserAccount userAccount) throws
            InterruptedException, ExecutionException {
        final CollectionReference collectionRef = firestore.collection(COLLECTION_NAME);
        final DocumentReference docRef = collectionRef.document();

        ApiFuture<WriteResult> result = docRef.create(userAccount);
        result.get();
        throw new UnsupportedOperationException("A faire");
    }
}