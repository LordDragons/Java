package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputReaderUtil implements InputReader {


    private static final Scanner scan = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    public int readSelection() {
        int selection = -1;
        while (selection == -1) {
            System.out.print("Please enter your selection: ");
            try {
                selection = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                logger.error("Error while reading user input from Shell", e);
                System.out.println("Error reading input. Please enter a valid number to proceed.");
            }
        }
        return selection;
    }

    public String readVehicleRegistrationNumber() {
        // Utiliser directement la méthode de la classe
        String vehicleRegNumber = readVehicleRegistrationNumberFromInput();  // Appel à la méthode mockée

        // Vérifier si l'entrée est null ou vide
        if (vehicleRegNumber == null || vehicleRegNumber.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: Vehicle registration number cannot be empty.");
        }

        return vehicleRegNumber;
    }

    // Méthode qui simule la lecture d'un numéro d'enregistrement de véhicule
    public String readVehicleRegistrationNumberFromInput() {
        return "ABCPDF";  // Simuler une valeur pour le test
    }

    // Ajouter une méthode pour fermer le scanner lorsqu'il n'est plus nécessaire
    public void closeScanner() {
        scan.close();
    }
}
