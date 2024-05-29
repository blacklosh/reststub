package ru.itis.reststub.services;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itis.reststub.exceptions.auth.NotLoggedInException;

@UtilityClass
public class CentralAuth {

    public String getCurrentUserName() {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails e) {
            return e.getUsername();
        }
        throw new NotLoggedInException();
    }
}
