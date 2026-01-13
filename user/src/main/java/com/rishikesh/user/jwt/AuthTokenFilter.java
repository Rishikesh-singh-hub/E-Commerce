//package com.rishikesh.user.jwt;
//
//import com.nimbusds.jwt.JWTClaimsSet;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.List;
//
//@Component
//public class AuthTokenFilter extends OncePerRequestFilter {
//
//    Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
//    private final  JwtUtils jwtUtils;
//    private final AuthEntryPointJwt authEntryPointJwt;
//
//    public AuthTokenFilter(JwtUtils jwtUtils,
//                           AuthEntryPointJwt authEntryPointJwt) {
//        this.jwtUtils = jwtUtils;
//        this.authEntryPointJwt = authEntryPointJwt;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws  IOException,
//            InvalidOneTimeTokenException {
//
//        logger.info("Searching for jwt ");
//        try{
//            String token = extractToken(request);
//            if(token != null){
//                Authentication auth = authenticate(token,request);
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//            filterChain.doFilter(request,response);
//
//        }catch (Exception ex) {
//            SecurityContextHolder.clearContext();
//            authEntryPointJwt.commence(request, response, (AuthenticationException) ex);
//        }
//
//
//    }
//
//    private String extractToken(HttpServletRequest request){
//        String header = request.getHeader("Authorization");
//
//        if(header == null || !header.startsWith("Bearer ")){
//            return null;
//        }
//
//        return header.substring(7).trim();
//
//    }
//
//    private Authentication authenticate(String token,
//                                        HttpServletRequest req) throws Exception {
//        JWTClaimsSet claim = jwtUtils.verifyAndParse(token);
//        String userId = claim.getSubject();
//
//        List<String> roles = claim.getStringListClaim("roles");
//        if(roles != null){
//            roles = List.of();
//        }
//
//        Collection<SimpleGrantedAuthority> authorities = roles.stream()
//                .map(SimpleGrantedAuthority :: new)
//                .toList();
//
//        return new UsernamePasswordAuthenticationToken(
//                userId,
//                null,
//                authorities
//        );
//    }
//
//
//}
//
////private void writeErrorResponse(HttpServletResponse res, int statusCode, String message) throws IOException {
////    ErrorResponse body = ErrorResponse.builder()
////            .code(statusCode)
////            .message(message)
////            .build();
////
////    res.setStatus(statusCode);
////    res.setContentType("application/json");
////    res.setCharacterEncoding("UTF-8");
////
////    ObjectMapper objectMapper = new ObjectMapper();
////    String json = objectMapper.writeValueAsString(body);
////    res.getWriter().write(json);
////
////
////}
//
//
////try{
////String header = request.getHeader("Authorization");
////            if (header != null && header.startsWith("Bearer ")) {
////String token = header.substring(7).trim();
////                if (token.isEmpty()) {
////        throw new IllegalArgumentException("empty token");
////                }
////
////// Let Jwt library exceptions bubble up here (ExpiredJwtException, MalformedJwtException, SignatureException...)
////Claims claims = jwtUtils.parseClaims(token);
////
////String userId = claims.getSubject();
////                if (userId == null || userId.isBlank()) {
////        throw new IllegalArgumentException("missing subject");
////                }
////UserEntity entity = userRepo.findById(userId).orElseThrow();
////UserDetails userDetails = userDetailsConfig.loadUserByUsername(entity.getEmail());
////                if (!jwtUtils.isTokenValidForUser(token, entity)) {
////        throw new InvalidTokenException("Token Invalid");
////                }
////
////UsernamePasswordAuthenticationToken auth =
////        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
////                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////        SecurityContextHolder.getContext().setAuthentication(auth);
////            }
////
////                    // If everything ok, continue
////                    }catch(MalformedJwtException | SignatureException | UnsupportedJwtException | InvalidTokenException e){
////        SecurityContextHolder.clearContext();
////            logger.warn("Invalid token: {}", e.toString());
////
////AuthenticationException authEx = new AuthenticationCredentialsNotFoundException("Invalid token", e);
////            authEntryPointJwt.commence(request, response, authEx);
////            return;
////                    } catch (ExpiredJwtException |
////IllegalArgumentException  |
////UsernameNotFoundException e
////        ) {
////                SecurityContextHolder.clearContext();
////            logger.warn(e.getMessage());
////AuthenticationException authEx = new JwtTokenExpiredException("Jwt_Token_Expired");
////            authEntryPointJwt.commence(request, response, authEx);
////            return;
////                    }
////                    filterChain.doFilter(request,response);
