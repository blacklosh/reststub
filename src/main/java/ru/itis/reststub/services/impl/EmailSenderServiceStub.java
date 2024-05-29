package ru.itis.reststub.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.reststub.services.EmailSenderService;

@Slf4j
@Service
public class EmailSenderServiceStub implements EmailSenderService {

    @Override
    public boolean sendConfirmCodeEmail(String email, String code) {
        log.warn("Using stub email service. Code {} sent to {}", code, email);
        return true;
    }
}
