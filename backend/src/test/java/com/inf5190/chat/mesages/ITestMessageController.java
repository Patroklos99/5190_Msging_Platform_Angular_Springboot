package com.inf5190.chat.mesages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.inf5190.chat.auth.model.LoginRequest;
import com.inf5190.chat.auth.model.LoginResponse;
import com.inf5190.chat.messages.model.Message;
import com.inf5190.chat.messages.repository.FirestoreMessage;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:firebase.properties")
public class ITestMessageController {
    private final FirestoreMessage message1 = new FirestoreMessage("u1", Timestamp.now(), "t1", null);
    private final FirestoreMessage message2 = new FirestoreMessage("u2", Timestamp.now(), "t1", null);

    @Value("${firebase.project.id}")
    private String firebaseProjectId;

    @Value("${firebase.emulator.port}")
    private String emulatorPort;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Firestore firestore;

    private String messagesEndpointUrl;
    private String loginEndpointUrl;

    @BeforeAll
    public static void checkRunAgainstEmulator() {
        checkEmulators();
    }

    @BeforeEach
    public void setup() throws InterruptedException, ExecutionException {
        this.messagesEndpointUrl = "http://localhost:" + port + "/messages";
        this.loginEndpointUrl = "http://localhost:" + port + "/auth/login";

        this.firestore.collection("messages").document("1")
                .create(this.message1).get();
        this.firestore.collection("messages").document("2")
                .create(this.message2).get();
    }

    @AfterEach
    public void testDown() {
        this.restTemplate.delete(
                "http://localhost:" + this.emulatorPort + "/emulator/v1/projects/"
                        + this.firebaseProjectId
                        + "/databases/(default)/documents");
    }

    @Test
    public void getMessageNotLoggedIn() {
        ResponseEntity<String> response = this.restTemplate.getForEntity(this.messagesEndpointUrl,
                String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getMessages() {
        final String token = this.login();

        final HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + token);

        final HttpEntity<Object> headers = new HttpEntity<Object>(header);
        final ResponseEntity<Message[]> response = this.restTemplate.exchange(this.messagesEndpointUrl,
                HttpMethod.GET, headers, Message[].class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody().length == 2);
        assertThat(response.getBody()[0].username().equals("u1"));
        assertThat(response.getBody()[1].username().equals("u2"));
    }

    @Test
    public void getMessagesMoreThan20() throws ExecutionException, InterruptedException {

        for (int i = 3; i < 30; i++)
            this.firestore.collection("messages").document(Integer.toString(i)).create(this.message1).get();
        final String token = this.login();

        final HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + token);

        final HttpEntity<Object> headers = new HttpEntity<Object>(header);
        final ResponseEntity<Message[]> response = this.restTemplate.exchange(this.messagesEndpointUrl,
                HttpMethod.GET, headers, Message[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(Objects.requireNonNull(response.getBody()).length == 20);
    }

    @Test
    public void getMessagesFromId() {
        final String token = this.login();

        final HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + token);

        final HttpEntity<Object> headers = new HttpEntity<Object>(header);
        final ResponseEntity<Message[]> response = this.restTemplate.exchange(this.messagesEndpointUrl + "?fromId=1",
                HttpMethod.GET, headers, Message[].class);


        assertThat(response.getBody().length == 1);
        assertThat(response.getBody()[0].username().equals("u2"));
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getMessagesFromIdInvalid() {
        final String token = this.login();

        final HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + token);

        final HttpEntity<Object> headers = new HttpEntity<Object>(header);
        final ResponseEntity<Object> response = this.restTemplate.exchange(this.messagesEndpointUrl + "?fromId=3",
                HttpMethod.GET, headers, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getMessagesBadToken() {
        final String token = "tatataa";

        final HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + token);

        final HttpEntity<Object> headers = new HttpEntity<>(header);
        final ResponseEntity<Object> response = this.restTemplate.exchange(this.messagesEndpointUrl,
                HttpMethod.GET, headers, Object.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void postMessagesBadToken() {
        String token = "tatata";
        final HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + token);

        final HttpEntity<Object> headers = new HttpEntity<Object>(header);
        final ResponseEntity<Object> response = this.restTemplate.exchange(this.messagesEndpointUrl,
                HttpMethod.POST, headers, Object.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    private String login() {
        LoginResponse response = this.restTemplate.postForObject(this.loginEndpointUrl,
                new LoginRequest("username", "password"),
                LoginResponse.class);

        return response.token();
    }

    private static void checkEmulators() {
        final String firebaseEmulator = System.getenv().get("FIRESTORE_EMULATOR_HOST");
        if (firebaseEmulator == null || firebaseEmulator.length() == 0) {
            System.err.println(
                    "**********************************************************************************************************");
            System.err.println(
                    "******** You need to set FIRESTORE_EMULATOR_HOST=localhost:8181 in your system properties. ********");
            System.err.println(
                    "**********************************************************************************************************");
        }
        assertThat(firebaseEmulator).as(
                        "You need to set FIRESTORE_EMULATOR_HOST=localhost:8181 in your system properties.")
                .isNotEmpty();
        final String storageEmulator = System.getenv().get("FIREBASE_STORAGE_EMULATOR_HOST");
        if (storageEmulator == null || storageEmulator.length() == 0) {
            System.err.println(
                    "**********************************************************************************************************");
            System.err.println(
                    "******** You need to set FIREBASE_STORAGE_EMULATOR_HOST=localhost:9199 in your system properties. ********");
            System.err.println(
                    "**********************************************************************************************************");
        }
        assertThat(storageEmulator).as(
                        "You need to set FIREBASE_STORAGE_EMULATOR_HOST=localhost:9199 in your system properties.")
                .isNotEmpty();
    }
}