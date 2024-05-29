package ru.itis.reststub.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.reststub.services.CodeGenerationService;

@Slf4j
@Service
public class CodeGenerationServiceStub implements CodeGenerationService {

    private static final String CODE = "1a2b3c4d";

    @Override
    public String getConfirmCode() {
        log.warn("Using code generation stub. Generated code is mock: {}", CODE);
        return CODE;
    }
}
