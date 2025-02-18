package proto.exam.map;

import proto.exam.map.model.Hero;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
        exportHeroesToJson(heroes);
    }

    private static void loadHeroesFromJson(List<Hero> heroes) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(HEROES_FILE);

        if (file.exists()) {
            try {
                // Lire le fichier JSON et le convertir en liste de héros
                Hero[] existingHeroes = objectMapper.readValue(file, Hero[].class);
                for (Hero hero : existingHeroes) {
                    heroes.add(hero);
                }
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

        System.out.println("Choisissez le type de héros : guerrier, archer ou magicien");
        String type = scanner.nextLine().toLowerCase();

        if (!isValidRace(race) || !isValidType(type)) {
            System.out.println("Entrée invalide. Veuillez réessayer.");
            return;
        }

        // Demander la couleur du héros
        System.out.println("Choisissez la couleur du héros parmi les suivantes :");
        System.out.println("1. Rouge\n2. Vert\n3. Bleu\n4. Noir\n5. Blanc");
        String colorChoice = scanner.nextLine().toLowerCase();
        String color = getColorFromChoice(colorChoice);

        Hero hero = new Hero(type, race);
        hero.setColor(color); // Assurez-vous que Pawn a une méthode setClasseCouleur()
        heroes.add(hero); // Ajouter le héros généré à la liste

        System.out.println("Héros généré avec succès !");
        System.out.println("Type : " + hero.getClasse());
        System.out.println("Race : " + hero.getRace());
        System.out.println("Vitesse : " + hero.getSpeed());
        System.out.println("Couleur : " + hero.getColor());
    }

    private static void displayHeroes(List<Hero> heroes) {
        if (heroes.isEmpty()) {
            System.out.println("Aucun héros généré.");
        } else {
            for (int i = 0; i < heroes.size(); i++) {
                Hero hero = heroes.get(i);
                System.out.println("\nHéros #" + (i + 1));
                System.out.println("Type : " + hero.getClasse());
                System.out.println("Race : " + hero.getRace());
                System.out.println("Vitesse : " + hero.getSpeed());
                System.out.println("Couleur : " + hero.getColor());
            }
        }
    }

    private static void deleteHero(Scanner scanner, List<Hero> heroes) {
        if (heroes.isEmpty()) {
            System.out.println("Aucun héros à supprimer.");
            return;
        }

        displayHeroes(heroes);
        System.out.println("\nEntrez le numéro du héros à supprimer :");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < heroes.size()) {
            heroes.remove(index);
            System.out.println("Héros supprimé avec succès.");
        } else {
            System.out.println("Numéro invalide. Aucun héros supprimé.");
        }
    }

    private static void clearHeroes(List<Hero> heroes) {
        heroes.clear();
        System.out.println("La liste des héros a été effacée.");
    }

    private static boolean isValidType(String type) {
        return type.equals("guerrier") || type.equals("archer") || type.equals("magicien");
    }

    private static boolean isValidRace(String race) {
        return race.equals("humain") || race.equals("nain") || race.equals("elfe");
    }

    // Méthode pour obtenir la couleur choisie par l'utilisateur
    private static String getColorFromChoice(String choice) {
        switch (choice) {
            case "1":
                return "rouge";
            case "2":
                return "vert";
            case "3":
                return "bleu";
            case "4":
                return "blanc";
            case "5":
                return "jaune";
            case "6":
                return "orange";
            case "7":
                return "violet";
            case "8":
                return "rose";
            case "9":
                return "marron";
            case "10":
                return "turquoise";
            default:
                return "gris"; // Valeur par défaut
        }
    }

    private static void exportHeroesToJson(List<Hero> heroes) {
        try (FileWriter file = new FileWriter(HEROES_FILE)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, heroes);
            System.out.println("Les détails des héros ont été exportés dans " + HEROES_FILE);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier JSON : " + e.getMessage());
        }
    }
}
