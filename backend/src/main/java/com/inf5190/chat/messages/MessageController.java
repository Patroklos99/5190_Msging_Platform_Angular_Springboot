package com.inf5190.chat.messages;

import com.inf5190.chat.auth.session.SessionDataAccessor;
import com.inf5190.chat.messages.model.Message;
import com.inf5190.chat.messages.repository.MessageRepository;
import com.inf5190.chat.websocket.WebSocketManager;

import jakarta.servlet.ServletContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur qui gère l'API de messages.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController implements ServletContextAware {
    public static final String MESSAGES_PATH = "/messages";
    private ServletContext servletContext;
    private MessageRepository messageRepository;
    private WebSocketManager webSocketManager;

    public MessageController(MessageRepository messageRepository,
                             WebSocketManager webSocketManager,
                             SessionDataAccessor sessionDataAccessor) {
        this.messageRepository = messageRepository;
        this.webSocketManager = webSocketManager;
    }

    @PostMapping(MESSAGES_PATH)
    public ResponseEntity<String> createMessage(@RequestBody Message msgBody) {
        messageRepository.createMessage(msgBody);
        webSocketManager.notifySessions();
        return ResponseEntity.status(HttpStatus.CREATED).body("\"Msg created\"");
    }

    @GetMapping(MESSAGES_PATH)
    public List<Message> getMessages() {
        return messageRepository.getMessages(null);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}