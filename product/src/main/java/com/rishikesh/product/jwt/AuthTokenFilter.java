package com.rishikesh.product.jwt;

import com.rishikesh.contracts.exception.InvalidTokenException;
import com.rishikesh.contracts.exception.JwtTokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final AuthEntryPoint authEntryPoint;
    private final JwtUtils jwtUtils;

    public AuthTokenFilter(AuthEntryPoint authEntryPoint,
                           JwtUtils jwtUtils) {
        this.authEntryPoint = authEntryPoint;
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        logger.info("Searching for JWT");
        try{
            String jwt = request.getHeader("Authorization");
            if(jwt!=null && jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7).trim();
                logger.info("jwt found {}", jwt);
            }

            if (jwt!= null && jwtUtils.validateJwt(jwt)) {
                logger.info("jwt validated");
                List<String> roles = jwtUtils.getRolesFromJwt(jwt);
                logger.info("got user roles from jwt {}",roles);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(
                                role ->new SimpleGrantedAuthority("ROLE_"+role)
                        ).toList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        null,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("SecurityContextHolder updated with roles {}", authorities);



            }
        }catch(MalformedJwtException | SignatureException | UnsupportedJwtException | InvalidTokenException e){
            SecurityContextHolder.clearContext();

            logger.warn("Invalid token: {}", e.toString());

            AuthenticationException authEx = new AuthenticationCredentialsNotFoundException("Invalid token", e);
            authEntryPoint.commence(request, response, authEx);
            return;
        } catch (ExpiredJwtException |
                 IllegalArgumentException  |
                 UsernameNotFoundException e
        ) {
            SecurityContextHolder.clearContext();
            logger.warn(e.getMessage());
            AuthenticationException authEx = new JwtTokenExpiredException("Jwt_Token_Expired");
            authEntryPoint.commence(request, response, authEx);
            return;
        }
        filterChain.doFilter(request,response);


    }

}