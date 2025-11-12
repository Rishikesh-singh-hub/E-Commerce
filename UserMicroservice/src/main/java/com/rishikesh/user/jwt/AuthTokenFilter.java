package com.rishikesh.user.jwt;

import com.rishikesh.user.configuration.UserDetailsConfig;
import com.rishikesh.user.entity.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final JwtUtils jwtUtils;
    private final UserDetailsConfig userDetailsConfig;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsConfig userDetailsConfig) {
        this.jwtUtils = jwtUtils;
        this.userDetailsConfig = userDetailsConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            logger.info("started JWT filter");
        try{
            String jwt = jwtUtils.getJwtFromHeader(request);


            if(jwt != null){


                logger.info("jwt spotted in bearer token");
                UserEntity entity = jwtUtils.getIdFromJwt(jwt).orElseThrow(() ->new UsernameNotFoundException("User not found !"));
                UserDetails userDetails = userDetailsConfig.loadUserByUsername(entity.getEmail());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);


            }
        }catch (MalformedJwtException  |
                ExpiredJwtException  |
                UnsupportedJwtException |
                IllegalArgumentException  |
                UsernameNotFoundException e
        ){logger.info(e.getMessage());}

        filterChain.doFilter(request,response);

    }
}
