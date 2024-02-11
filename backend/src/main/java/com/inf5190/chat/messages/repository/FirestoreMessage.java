package com.inf5190.chat.messages.repository;

import com.google.cloud.Timestamp;

import java.util.Objects;

public class FirestoreMessage {
    private String username;

    @Override
    public String toString() {
        return "FirestoreMessage{" +
                "username='" + username + '\'' +
                ", timestamp=" + timestamp +
                ", text='" + text + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirestoreMessage that = (FirestoreMessage) o;
        return Objects.equals(username, that.username) && Objects.equals(timestamp, that.timestamp) && Objects.equals(text, that.text) && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, timestamp, text, imageUrl);
    }

    private Timestamp timestamp;
    private String text;
    private String imageUrl;

    public FirestoreMessage() {
    }

    public FirestoreMessage(String username, Timestamp timestamp, String text, String imageUrl) {
        this.username = username;
        this.timestamp = timestamp;
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}