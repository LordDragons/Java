package com.eservice.ms_preference.controller;

import com.eservice.ms_preference.model.Preference;
import com.eservice.ms_preference.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    /**
     * Récupère les préférences d'un utilisateur spécifique via son ID.
     *
     * @param userId L'UUID de l'utilisateur.
     * @return Les préférences associées à l'utilisateur.
     */
    @GetMapping("/{userId}")
    public Preference getPreferencesByUserId(@PathVariable UUID userId) {
        return preferenceService.getPreferences();
    }

    /**
     * Récupère les préférences par défaut (sans ID utilisateur).
     *
     * @return Les préférences par défaut.
     */
    @GetMapping
    public Preference getPreferences() {
        return preferenceService.getPreferences();
    }

    /**
     * Met à jour les préférences globales ou spécifiques d'un utilisateur.
     *
     * @param newPreferences Les nouvelles préférences à appliquer.
     */
    @PutMapping
    public void updatePreferences(@RequestBody Preference newPreferences) {
        preferenceService.updatePreferences(newPreferences);
    }
}
