package ru.itis.reststub.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.reststub.dto.response.VersionResponse;
import ru.itis.reststub.services.VersionService;

@Slf4j
@Service
public class VersionServiceImpl implements VersionService {

    @Value("${app.actual-version}")
    private String actualVersion;

    @Value("${app.min-version}")
    private String minVersion;

    @Override
    public VersionResponse getCurrentVersion() {
        return VersionResponse.builder()
                .actualVersion(actualVersion)
                .minVersion(minVersion)
                .build();
    }
}
