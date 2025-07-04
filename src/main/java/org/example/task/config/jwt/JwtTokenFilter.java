package org.example.task.config.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.task.common.UserExtract;
import org.example.task.model.user.Users;
import org.example.task.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class JwtTokenFilter  extends OncePerRequestFilter {
    private final JwtUtil jwtTokenUtil;
    private final UserService userService;


    @Autowired
    public JwtTokenFilter(JwtUtil jwtTokenUtil,@Lazy UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String gmail = null;
        String phoneNumber = null;
        String provider = null;
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
            try {
                gmail = jwtTokenUtil.getGmailFromToken(token);
                phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(token);
                provider = jwtTokenUtil.getProviderFromToken(token);
            } catch (IllegalArgumentException | ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        // Check if the user is authenticated
        if (gmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Users user = userService.getUserDetails(phoneNumber,provider);
            if (jwtTokenUtil.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
    public UserExtract extractUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return new UserExtract(
                    UUID.fromString(jwtTokenUtil.getUserIdFromToken(token)),
                    jwtTokenUtil.getGmailFromToken(token),
                    jwtTokenUtil.getPhoneNumberFromToken(token),
                    jwtTokenUtil.getChatIdFromToken(token),
                    jwtTokenUtil.getRolesFromToken(token),
                    jwtTokenUtil.getProviderFromToken(token)

            );
        }
        throw new IllegalArgumentException("Authorization token is missing or invalid");
    }
}
