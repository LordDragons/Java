package SafetyNet.alerts.dto;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AgeCalculatorDTO {

    public static int calculateAge(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try {
            LocalDate birthDate = LocalDate.parse(birthDateString, formatter);

            if (birthDate.isAfter(LocalDate.now())) {
                System.err.println("La date de naissance ne peut pas être dans le futur : " + birthDateString);
                return 0; // Returning 0 or some default value
            }

            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (DateTimeParseException e) {
            System.err.println("Format de date invalide : " + birthDateString);
            return 0; // Handle parsing error by returning 0
        }
    }

    }


//    public static void main(String[] args) {
//        // Exemple de chaîne provenant d'un fichier JSON
//        String jsonDate = "04/15/1990";
//
//        int age = calculateAge(jsonDate);
//       if (age != -1) {
//           System.out.println("L'âge calculé est : " + age + " ans.");
//        } else {
//            System.out.println("Impossible de calculer l'âge à partir de la date donnée.");
//        }
//    }



