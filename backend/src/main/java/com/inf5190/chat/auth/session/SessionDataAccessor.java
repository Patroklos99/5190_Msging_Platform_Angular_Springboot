package com.inf5190.chat.auth.session;

import jakarta.servlet.ServletContext;

import org.springframework.stereotype.Component;

/**
 * Classe utilitaire qui permet de récupérer les informations du contexte de la
 * requête.
 */
@Component
public class SessionDataAccessor {
    private static final String TOKEN_KEY = "TOKEN_KEY";
    private static final String SESSION_DATA_KEY = "SESSION_DATA_KEY";

    public void setSessionData(ServletContext context, SessionData sessionData) {
        context.setAttribute(SESSION_DATA_KEY, sessionData);
    }

    public void setToken(ServletContext context, String token) {
        context.setAttribute(TOKEN_KEY, token);
    }

    public SessionData getSessionData(ServletContext context) {
        return (SessionData) context.getAttribute(SESSION_DATA_KEY);
    }

    public String getToken(ServletContext context) {
        return (String) context.getAttribute(TOKEN_KEY);
    }

}
