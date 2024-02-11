package com.inf5190.chat.auth;

import com.inf5190.chat.auth.session.SessionData;
import jakarta.servlet.ServletContext;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;

import com.inf5190.chat.auth.model.LoginRequest;
import com.inf5190.chat.auth.model.LoginResponse;
import com.inf5190.chat.auth.session.SessionDataAccessor;
import com.inf5190.chat.auth.session.SessionManager;
import com.inf5190.chat.auth.repository.UserAccountRepository;
import com.inf5190.chat.auth.repository.FirestoreUserAccount;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * Contrôleur qui gère l'API de login et logout.
 * <p>
 * Implémente ServletContextAware pour recevoir le contexte de la requête HTTP.
 */
@RestController()
public class AuthController implements ServletContextAware {

    private final SessionManager sessionManager;
    private final SessionDataAccessor sessionDataAccessor;
    private ServletContext servletContext;

    private UserAccountRepository userAcctRepo;
    private PasswordEncoder passEncoder;

    public AuthController(SessionManager sessionManager, SessionDataAccessor sessionDataAccessor,
                          UserAccountRepository userAcctRepo, PasswordEncoder passEncoder) {
        this.sessionManager = sessionManager;
        this.sessionDataAccessor = sessionDataAccessor;
        this.userAcctRepo = userAcctRepo;
        this.passEncoder = passEncoder;
    }

    @PostMapping("auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws ExecutionException, InterruptedException {
        FirestoreUserAccount client = this.userAcctRepo.getUserAccount(loginRequest.username());
        if (client != null) {
            if (this.passEncoder.matches(loginRequest.password(), client.getEncodedPassword())) {
                SessionData sessionData = new SessionData(loginRequest.username());
                String token = sessionManager.addSession(sessionData);
//        return new LoginResponse(token);
                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

        }
        String hashedPass = this.passEncoder.encode(loginRequest.password());
        FirestoreUserAccount newAcct = new FirestoreUserAccount();
        newAcct.setUsername(loginRequest.username());
        newAcct.setEncodedPassword(hashedPass);
        userAcctRepo.setUserAccount(newAcct);

        SessionData sessionData = new SessionData(loginRequest.username());
        String token = sessionManager.addSession(sessionData);
//        return new LoginResponse(token);
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
    }

    @PostMapping("auth/logout")
    public void logout() {
        try {
            String token = sessionDataAccessor.getToken(servletContext);
            sessionManager.removeSession(token);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error on logout");
        }
    }

    @Override
    public void setServletContext(jakarta.servlet.ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
