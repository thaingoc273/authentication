package com.customerservice.authentication.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.customerservice.authentication.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.AuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    // This class will be responsible for filtering incoming requests and performing JWT authentication.
    // It will typically extend OncePerRequestFilter from Spring Security.
    // Key functionalities:
    // 1. Extract JWT from the request header.
    // 2. Validate the JWT.
    // 3. Parse claims from the JWT to get user details.
    // 4. Set the authenticated user in the SecurityContext.
    // 5. Continue the filter chain.

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) 
        throws ServletException, IOException{
            String authHeader = request.getHeader("Authorization");
            String username = null;
            String jwt = null;
            try {
                if (authHeader != null && authHeader.startsWith("Bearer ")){
                    jwt = authHeader.substring(7);
                    username = jwtUtil.getUsernameFromToken(jwt);
                }
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                sendErrorResponse(response, "JWT token has expired. Please login again.", 401);
            } catch (MalformedJwtException e) {
                sendErrorResponse(response, "Invalid JWT token format.", 401);
            } catch (UnsupportedJwtException e) {
                sendErrorResponse(response, "Unsupported JWT token.", 401);
            } catch (SignatureException | SecurityException e) {
                sendErrorResponse(response, "Invalid JWT signature.", 401);
            } catch (IllegalArgumentException e) {
                sendErrorResponse(response, "JWT claims string is empty.", 401);
            } catch (AuthenticationException e) {
                sendErrorResponse(response, "Authentication failed. Please provide valid credentials.", 401);
            }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(java.util.Collections.singletonMap("message", message)));
    }
}
