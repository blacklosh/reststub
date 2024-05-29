package ru.itis.reststub.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.reststub.dto.response.ErrorResponse;
import ru.itis.reststub.exceptions.ErrorCode;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static ru.itis.reststub.security.config.SecurityConfig.IGNORE;
import static ru.itis.reststub.security.config.SecurityConfig.PERMIT_ALL;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${app.jwt.secret_key}")
    private String secretKey;

    private final ObjectMapper mapper;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        for(String path : IGNORE) {
            if(request.getRequestURI().startsWith(path.replaceAll("/\\*\\*", ""))) {
                log.info("Input request without jwt allowed to " + request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
        }

        for(String path : PERMIT_ALL) {
            if(request.getRequestURI().startsWith(path.replaceAll("/\\*\\*", ""))) {
                log.info("Input request without jwt allowed to " + request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
        }

        int code = 602;
        String message = "Your bearer token bad/missed";
        String token = request.getHeader(AUTHORIZATION);
        int result = isValidAccessToken(token);

        if(result == 0) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(getUsernameFromAccessToken(parseToken(token)));
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
            filterChain.doFilter(request, response);
        } else {
            SecurityContextHolder.clearContext();
            response.setCharacterEncoding("UTF-8");
            response.setStatus(code);
            response.setHeader("Content-Type", "application/json");
            try {
                response.getWriter().write(mapper.writeValueAsString(
                        ErrorResponse.builder()
                                .errorCode(ErrorCode.NO_BEARER.name())
                                .errorDescription(message)
                                .build()
                ));
            } catch (Exception e) {
                log.error("Got exception {} on {} by jwt filter", e.getMessage(), request.getRequestURI());
            }
        }
    }

    private String getUsernameFromAccessToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    private String parseToken(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        return token.substring(TOKEN_PREFIX.length());
    }

    private int isValidAccessToken(String token) {
        log.debug(String.format("Validating access token %s", token));
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            log.warn(String.format("Token %s is not valid!", token));
            return 1;
        }
        try {
            String finalToken = parseToken(token);
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(finalToken).getBody().getSubject();
            log.debug(String.format("Token %s  valid", token));
            return 0;
        } catch (ExpiredJwtException e) {
            log.warn("Token expired");
            return  2;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn(String.format("Token %s is not valid!", token));
            return  3;
        }
    }
}
