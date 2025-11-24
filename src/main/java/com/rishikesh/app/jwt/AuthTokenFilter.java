package com.rishikesh.app.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.rishikesh.app.configuration.UserDetailsConfig;
import com.rishikesh.app.exception.ErrorResponse;
import com.rishikesh.app.entity.UserEntity;
import com.rishikesh.app.exception.InvalidTokenException;
import com.rishikesh.app.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final  JwtUtils jwtUtils;
    private final UserDetailsConfig userDetailsConfig;
    private final UserRepo userRepo;
    private final AuthEntryPointJwt authEntryPointJwt;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsConfig userDetailsConfig, UserRepo userRepo, AuthEntryPointJwt authEntryPointJwt) {
        this.jwtUtils = jwtUtils;
        this.userDetailsConfig = userDetailsConfig;
        this.userRepo = userRepo;
        this.authEntryPointJwt = authEntryPointJwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, InvalidOneTimeTokenException {
            logger.info("Searching for jwt ");
        try{
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7).trim();
                if (token.isEmpty()) {
                    throw new IllegalArgumentException("empty token");
                }

                // Let Jwt library exceptions bubble up here (ExpiredJwtException, MalformedJwtException, SignatureException...)
                Claims claims = jwtUtils.parseClaims(token);

                String userId = claims.getSubject();
                if (userId == null || userId.isBlank()) {
                    throw new IllegalArgumentException("missing subject");
                }
                UserEntity entity = userRepo.findById(userId).orElseThrow();
                UserDetails userDetails = userDetailsConfig.loadUserByUsername(entity.getEmail());
                if (!jwtUtils.isTokenValidForUser(token, entity)) {
                    throw new InvalidTokenException("Token Invalid");
                }

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            // If everything ok, continue
            filterChain.doFilter(request, response);
        }catch(MalformedJwtException | SignatureException | UnsupportedJwtException | InvalidTokenException e){
            SecurityContextHolder.clearContext();
            logger.warn("Invalid token: {}", e.toString());

            AuthenticationException authEx = new AuthenticationCredentialsNotFoundException("Invalid token", e);
            authEntryPointJwt.commence(request, response, authEx);
            return;
        } catch (ExpiredJwtException |
                IllegalArgumentException  |
                UsernameNotFoundException e
        ){logger.info(e.getMessage());}

        filterChain.doFilter(request,response);

    }


    private void writeErrorResponse(HttpServletResponse res, int statusCode, String message) throws IOException {
        ErrorResponse body = ErrorResponse.builder()
                .code(statusCode)
                .message(message)
                .build();

        res.setStatus(statusCode);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(body);
        res.getWriter().write(json);


    }

}
