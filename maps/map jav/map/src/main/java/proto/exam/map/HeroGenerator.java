package proto.exam.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import proto.exam.map.model.Hero;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class HeroGenerator {

    private static final String HEROES_FILE = "heroes.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Hero> heroes = new ArrayList<>();

        // Charger les héros existants depuis le fichier JSON
        loadHeroesFromJson(heroes);

        System.out.println("Bienvenue dans le générateur de héros !");

        boolean continueGenerating = true;

        while (continueGenerating) {
            System.out.println("\nQue voulez-vous faire ?");
            System.out.println("1. Générer un nouveau héros");
            System.out.println("2. Afficher les héros générés");
            System.out.println("3. Supprimer un héros");
            System.out.println("4. Effacer la liste des héros");
            System.out.println("5. Quitter");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    generateHero(scanner, heroes);
                    break;
                case "2":
                    displayHeroes(heroes);
                    break;
                case "3":
                    deleteHero(scanner, heroes);
                    break;
                case "4":
                    clearHeroes(heroes);
                    break;
                case "5":
                    continueGenerating = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

        // Exporter les données des héros dans un fichier JSON
        try (FileWriter file = new FileWriter("heroes.json")) {
            file.write("[\n"); // Début du tableau JSON
            for (int i = 0; i < heroes.size(); i++) {
                Hero hero = heroes.get(i);
                file.write("  {\n");
                file.write("    \"id\": \"" + hero.getId() + "\",\n");
                file.write("    \"race\": \"" + hero.getId().split("_")[1] + "\",\n"); // Récupérer la race à partir de l'ID
                file.write("    \"type\": \"" + hero.getId().split("_")[0] + "\",\n"); // Récupérer le type à partir de l'ID
                file.write("    \"speed\": " + hero.getSpeed() + ",\n");
                file.write("    \"color\": \"" + hero.getColor() + "\"\n");
                if (i < heroes.size() - 1) {
                    file.write("  },\n");
                } else {
                    file.write("  }\n");
                }
            }
            file.write("]"); // Fin du tableau JSON
            System.out.println("Les détails des héros ont été exportés dans heroes.json.");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier JSON : " + e.getMessage());
        }
    }

    private static void loadHeroesFromJson(List<Hero> heroes) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(HEROES_FILE);

        if (file.exists()) {
            try {
                // Lire le fichier JSON et le convertir en liste de héros
                Hero[] existingHeroes = objectMapper.readValue(file, Hero[].class);
                heroes.addAll(Arrays.asList(existingHeroes));
                System.out.println("Héros existants chargés depuis " + HEROES_FILE);
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun fichier de héros trouvé, création d'un nouveau fichier.");
        }
    }

    private static void generateHero(Scanner scanner, List<Hero> heroes) {
        System.out.println("Choisissez la race du héros : humain, nain ou elfe");
        String race = scanner.nextLine().toLowerCase();

        System.out.println("Choisissez le type de héros : guerrier, archer, magicien ou soigneur");
        String type = scanner.nextLine().toLowerCase();

        if (!isValidRace(race) || !isValidType(type)) {
            System.out.println("Entrée invalide. Veuillez réessayer.");
            return; // Redemander les entrées si elles sont invalides
        }

        System.out.println("Choisissez la couleur du héros parmi les suivantes :");
        System.out.println("1. Rouge\n2. Vert\n3. Bleu\n4. Blanc\n5. Jaune\n6. Orange\n7. Violet\n8. Rose\n9. Marron\n10. Turquoise");
        String colorChoice = scanner.nextLine().toLowerCase();
        String color = getColorFromChoice(colorChoice);

        // Créer un nouveau héros
        Hero hero = new Hero(type + "_" + race, 0, 0, color, 5, race, type);
        hero.setId(type + "_" + race); // Créer un ID unique pour le héros
        hero.setColor(color);
        hero.setSpeed(5); // Exemple de vitesse, à adapter selon le type de héros

        // Ajout des héros à la liste
        heroes.add(hero);

        System.out.println("Héros généré avec succès !");
        System.out.println("ID : " + hero.getId());
        System.out.println("Race : " + race);
        System.out.println("Type : " + type);
        System.out.println("Vitesse : " + hero.getSpeed());
        System.out.println("Couleur : " + hero.getColor());
    }

    private static void displayHeroes(List<Hero> heroes) {
        if (heroes.isEmpty()) {
            System.out.println("Aucun héros n'a été généré.");
        } else {
            for (Hero hero : heroes) {
                System.out.println("ID : " + hero.getId());
                System.out.println("Race : " + hero.getId().split("_")[1]);
                System.out.println("Type : " + hero.getId().split("_")[0]);
                System.out.println("Vitesse : " + hero.getSpeed());
                System.out.println("Couleur : " + hero.getColor());
                System.out.println("---");
            }
        }
    }

    private static void deleteHero(Scanner scanner, List<Hero> heroes) {
        if (heroes.isEmpty()) {
            System.out.println("Aucun héros à supprimer.");
            return;
        }

        System.out.println("Entrez l'ID du héros à supprimer :");
        String heroId = scanner.nextLine();

        Hero heroToDelete = heroes.stream()
                .filter(hero -> hero.getId().equals(heroId))
                .findFirst()
                .orElse(null);

        if (heroToDelete != null) {
            heroes.remove(heroToDelete);
            System.out.println("Héros supprimé avec succès.");
        } else {
            System.out.println("Héros non trouvé.");
        }
    }

    private static void clearHeroes(List<Hero> heroes) {
        heroes.clear();
        System.out.println("La liste des héros a été effacée.");
    }

    private static boolean isValidType(String type) {
        return type.equals("guerrier") || type.equals("archer") || type.equals("magicien") || type.equals("soigneur");
    }

    private static boolean isValidRace(String race) {
        return race.equals("humain") || race.equals("nain") || race.equals("elfe");
    }

    // Méthode pour obtenir la couleur choisie par l'utilisateur
    private static String getColorFromChoice(String choice) {
        return switch (choice) {
            case "1" -> "rouge";
            case "2" -> "vert";
            case "3" -> "bleu";
            case "4" -> "blanc";
            case "5" -> "jaune";
            case "6" -> "orange";
            case "7" -> "violet";
            case "8" -> "rose";
            case "9" -> "marron";
            case "10" -> "turquoise";
            default -> "gris"; // Valeur par défaut
        };
    }
}

