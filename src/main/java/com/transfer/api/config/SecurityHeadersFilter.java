package com.transfer.api.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;

        // 🔐 Proteções básicas
        res.setHeader("X-Content-Type-Options", "nosniff");
        res.setHeader("X-Frame-Options", "DENY");
        res.setHeader("X-XSS-Protection", "1; mode=block");

        // 🔐 HTTPS only (cuidado em dev)
        res.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // 🔐 Controle de recursos
        res.setHeader("Cross-Origin-Resource-Policy", "same-origin");

        // 🔐 CSP (versão simples)
        res.setHeader("Content-Security-Policy", "default-src 'self'");

        chain.doFilter(request, response);
    }
}