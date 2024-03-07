package com.inf5190.chat.auth.session;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;

/**
 * Classe qui gère les sessions utilisateur.
 * <p>
 * Pour le moment, on gère en mémoire.
 */
@Repository
public class SessionManager {
    private static final String SECRET_KEY_BASE64 = "8bg27XEzTiSMe/2PDCY1DtqNxYjfh0Vpa+yr+gSDEjU=";
    //    private static final String SECRET_KEY_BASE64 = "VUT407hwh7SKAivPDNgjwTD08KNFB58WL46jPRXgrKQ=";
    private final SecretKey secretKey;
    private final JwtParser jwtParser;
    private final Map<String, SessionData> sessions = new HashMap<String, SessionData>();

    public SessionManager() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_BASE64));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
    }

    public String addSession(SessionData authData) {
        JwtBuilder token = Jwts.builder()
                .setAudience("Chat-app")
                .setIssuedAt(new Date())
                .setSubject(authData.username())
                .setExpiration(Date.from(Instant.now().plusSeconds(7200)))
                .signWith(this.secretKey);

        String sessionId = generateSessionId();// Generate a unique session ID
        sessions.put(sessionId, authData);// Include the session data in the sessions map
        return token.compact();
    }

    public void removeSession(String sessionId) {
        this.sessions.remove(sessionId);
    }

    public SessionData getSession(String sessionId) {
        try {
            Claims claims = this.jwtParser.parseClaimsJws(sessionId).getBody();
            String username = claims.getSubject();
            return new SessionData(username);
        } catch (JwtException e) {
            return null;
        }
//        return this.sessions.get(sessionId);
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
