package ru.itis.reststub.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.reststub.dto.response.ErrorResponse;
import ru.itis.reststub.exceptions.ErrorCode;
import ru.itis.reststub.exceptions.auth.NotConfirmedException;
import ru.itis.reststub.exceptions.auth.NotLoggedInException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OtherErrorsFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (NotConfirmedException e) {
            showError(request, response, e, 604, ErrorCode.NOT_CONFIRMED.name());
        } catch (NotLoggedInException e) {
            showError(request, response, e, 605, ErrorCode.NOT_LOGGED_IN.name());
        }catch (Exception e) {
            showError(request, response, e, 603, e.getMessage());
        }
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, Exception e, int status, String code) {
        e.printStackTrace();
        SecurityContextHolder.clearContext();
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        try {
            response.getWriter().write(mapper.writeValueAsString(
                    ErrorResponse.builder()
                            .errorCode(code)
                            .errorDescription(e.getMessage())
                            .build()
            ));
        } catch (Exception i) {
            log.error("Unexpected and unprocessable error: {} at url {}. E was {}", i.getMessage(), request.getRequestURI(), e.getMessage());
        }
    }
}
