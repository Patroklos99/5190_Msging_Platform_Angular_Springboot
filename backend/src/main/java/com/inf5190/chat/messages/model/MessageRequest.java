package com.inf5190.chat.messages.model;

/**
 * Représente un message.
 */
public record MessageRequest(String username, String text, ChatImageDate imageDate) {
}
