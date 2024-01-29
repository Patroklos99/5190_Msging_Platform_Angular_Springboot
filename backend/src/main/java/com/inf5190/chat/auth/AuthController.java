package com.inf5190.chat.auth;

import com.inf5190.chat.auth.session.SessionData;
import jakarta.servlet.ServletContext;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;

import com.inf5190.chat.auth.model.LoginRequest;
import com.inf5190.chat.auth.model.LoginResponse;
import com.inf5190.chat.auth.session.SessionDataAccessor;
import com.inf5190.chat.auth.session.SessionManager;

import javax.servlet.http.HttpServletRequest;

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

    public AuthController(SessionManager sessionManager, SessionDataAccessor sessionDataAccessor) {
        this.sessionManager = sessionManager;
        this.sessionDataAccessor = sessionDataAccessor;
    }

    @PostMapping("auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        SessionData sessionData = new SessionData(loginRequest.username());
        String token = sessionManager.addSession(sessionData);
        return new LoginResponse(token);
    }

    @PostMapping("auth/logout")
    public void logout() {
        String token = sessionDataAccessor.getToken(servletContext);
        sessionManager.removeSession(token);
    }

    @Override
    public void setServletContext(jakarta.servlet.ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
