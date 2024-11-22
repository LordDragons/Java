package SafetyNet.alerts.config;

import SafetyNet.alerts.SafetyNetApplication;
import SafetyNet.alerts.models.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class BeanConfiguration {


    @Bean
    public Data data() {
        try {
            // Créer un ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Lire le fichier JSON depuis les ressources
            InputStream inputStream = SafetyNetApplication.class.getResourceAsStream("/data.json");

            // Vérifiez si le fichier existe
            if (inputStream == null) {
                throw new RuntimeException("Fichier data.json non trouvé dans les ressources !");
            }

            // Convertir le contenu JSON en objet Data
            Data data = objectMapper.readValue(inputStream, Data.class);

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading data.json: " + e.getMessage(), e);

        }
    }

}
