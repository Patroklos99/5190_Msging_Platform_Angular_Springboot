package com.inf5190.chat.auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.FilterConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.inf5190.chat.auth.session.SessionData;
import com.inf5190.chat.auth.session.SessionDataAccessor;
import com.inf5190.chat.auth.session.SessionManager;

/**
 * Filtre qui intercepte les requêtes HTTP et valide si elle est autorisée.
 */
public class AuthFilter implements Filter, jakarta.servlet.Filter {
    private static final String BEARER = "Bearer";

    private final SessionDataAccessor sessionDataAccessor;
    private final SessionManager sessionManager;

    public AuthFilter(SessionDataAccessor sessionDataAccessor, SessionManager sessionManager) {
        this.sessionDataAccessor = sessionDataAccessor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Si c'est la méthode OPTIONS on laisse passer. C'est une requête
        // pre-flight pour les CORS.
        if (httpRequest.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            chain.doFilter(request, response);
            return;
        }

        // Si la requête ne contient pas l'en-tête AUTHORIZATION ou ne contient pas le
        // Bearer, on n'accepte pas la requête.
        final String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            this.sendAuthErrorResponse(httpRequest, httpResponse);
            return;
        }

        // On vérifie si le token est valide sinon on n'accepte pas la
        // requête.
        final String[] authParts = authHeader.split(" ");
        if (authParts.length != 2 && !this.isValidToken(authParts[1])) {
            this.sendAuthErrorResponse(httpRequest, httpResponse);
            return;
        }

        final String token = authParts[1];
        final SessionData data = this.sessionManager.getSession(token);

        // Si on ne trouve pas la session, on n'accepte pas la requête.
        if (data == null) {
            this.sendAuthErrorResponse(httpRequest, httpResponse);
            return;
        }

        // On place le token et les données de session dans le contexte
        // pour que les contrôleurs puissent y avoir accès.
        this.sessionDataAccessor.setToken(httpRequest.getServletContext(), token);
        this.sessionDataAccessor.setSessionData(httpRequest.getServletContext(), data);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean isValidToken(String token) {
        // Pour le moment on accepte tous les tokens.
        return true;
    }

    protected void sendAuthErrorResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().contains("auth/logout")) {
            // Si c'est pour le logout, on retourne simplement 200 OK.
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws jakarta.servlet.ServletException {
        jakarta.servlet.Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest servletRequest, jakarta.servlet.ServletResponse servletResponse, jakarta.servlet.FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {

    }
}
