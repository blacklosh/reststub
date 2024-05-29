package ru.itis.reststub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.reststub.dto.response.VersionResponse;
import ru.itis.reststub.services.VersionService;

@RestController
@RequestMapping("/api/v1/version")
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;

    @GetMapping
    public VersionResponse getCurrentVersion() {
        return versionService.getCurrentVersion();
    }
}
