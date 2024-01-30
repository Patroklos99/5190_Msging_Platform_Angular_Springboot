package com.inf5190.chat.auth.repository;

import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Repository;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Repository
public class UserAccountRepository {
    private static final String COLLECTION_NAME = "userAccounts";
    private final Firestore firestore = FirestoreClient.getFirestore();

    public FirestoreUserAccount getUserAccount(String username) throws
            InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("A faire");
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