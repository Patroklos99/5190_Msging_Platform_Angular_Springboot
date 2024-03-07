package com.inf5190.chat.auth.session;

//import javax.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionDataAccessor {
    private static final String TOKEN_KEY = "TOKEN_KEY";
    private static final String SESSION_DATA_KEY = "SESSION_DATA_KEY";

    private HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        throw new IllegalStateException("Could not find the current HttpServletRequest");
    }

    public void setSessionData(HttpServletRequest request, SessionData sessionData) {
        getCurrentRequest().setAttribute(SESSION_DATA_KEY, sessionData);
    }

    public void setToken(HttpServletRequest request, String token) {
        getCurrentRequest().setAttribute(TOKEN_KEY, token);
    }

    public SessionData getSessionData(HttpServletRequest request) {
        return (SessionData) request.getAttribute(SESSION_DATA_KEY);
    }

    public String getToken() {
        return (String) getCurrentRequest().getAttribute(TOKEN_KEY);
    }
}

