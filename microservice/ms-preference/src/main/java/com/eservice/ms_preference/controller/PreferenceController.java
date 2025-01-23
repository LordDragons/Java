package com.eservice.ms_preference.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eservice.ms_preference.service.PreferenceService;

import java.util.UUID;

@RestController
@RequestMapping("/preferences")
public class PreferenceController {


    private PreferenceService preferenceService;

    @GetMapping("/{userId}")
    public PreferenceService getPreferences(@PathVariable UUID userId) {

        return preferenceService;
    }

}

