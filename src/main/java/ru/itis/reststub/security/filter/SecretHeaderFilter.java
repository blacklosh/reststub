package ru.itis.reststub.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.reststub.dto.response.ErrorResponse;
import ru.itis.reststub.exceptions.ErrorCode;

import java.io.IOException;

import static ru.itis.reststub.security.config.SecurityConfig.IGNORE;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecretHeaderFilter extends OncePerRequestFilter {

    @Value("${app.secret-header-name}")
    private String secretHeaderName;

    @Value("${app.secret-header-value}")
    private String secretHeaderValue;

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        for(String path : IGNORE) {
            if(request.getRequestURI().startsWith(path.replaceAll("/\\*\\*", ""))) {
                log.info("Input request without header allowed to " + request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
        }

        if(secretHeaderValue.equals(request.getHeader(secretHeaderName))) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Input request rejected to " + request.getRequestURI());
            SecurityContextHolder.clearContext();
            response.setCharacterEncoding("UTF-8");
            response.setStatus(601);
            response.setHeader("Content-Type", "application/json");
            response.getWriter().write(mapper.writeValueAsString(
                    ErrorResponse.builder()
                            .errorCode(ErrorCode.UNSUPPORTED_APPLICATION.name())
                            .build()
            ));
        }
    }
}
