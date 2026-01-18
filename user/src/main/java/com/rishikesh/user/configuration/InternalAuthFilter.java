package com.rishikesh.user.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class InternalAuthFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(InternalAuthFilter.class);
    @Value("${internal.secret}")
    private String INTERNAL_TOKEN;

    @Override
    public void doFilterInternal(HttpServletRequest req,
                                 HttpServletResponse res,
                                 FilterChain filterChain)throws ServletException, IOException {

        logger.info(INTERNAL_TOKEN);
        logger.info("user class received request on api: {}",req.getRequestURI());
        if(req.getRequestURI().startsWith("/internal/")){
            String token = req.getHeader("X-internal-secret");
            if (token == null || !token.equals(INTERNAL_TOKEN)) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(req, res);

    }

}
