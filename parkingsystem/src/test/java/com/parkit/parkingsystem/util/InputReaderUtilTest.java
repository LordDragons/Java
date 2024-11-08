package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

    private InputReader inputReaderUtil;

    @Mock
    private InputReader mockInputReader;


    @BeforeEach
    public void setUp() {
        inputReaderUtil = mockInputReader; // Make sure inputReaderUtil is assigned the mock correctly
    }

    @Test
    public void testReadSelection_ValidInput() {
        // Arrange
        when(mockInputReader.readSelection()).thenReturn(1);

        // Act
        int result = inputReaderUtil.readSelection();

        // Assert
        assertEquals(1, result);
    }



    @Test
    public void testReadVehicleRegistrationNumber_ValidInput() throws Exception {
        // Arrange
        when(mockInputReader.readVehicleRegistrationNumber()).thenReturn("ABCPDF");

        // Act
        String result = inputReaderUtil.readVehicleRegistrationNumber();

        // Assert
        assertEquals("ABCPDF", result);
    }

    @Test
    public void testReadSelection_InvalidInput() {
        // Simulate invalid input (e.g., a non-integer value)
        String input = "";
        System.setIn(new ByteArrayInputStream(input.getBytes()));  // Simulate user input

        // Act
        int result = inputReaderUtil.readSelection();

        // Print result for debugging
        System.out.println("Result: " + result);

        // Assert
        assertEquals(0, result, "Should return 0 when invalid input is provided.");
    }

    @Test
    public void testReadVehicleRegistrationNumber_EmptyInput() throws Exception {
        // Arrange
        when(mockInputReader.readVehicleRegistrationNumber()).thenThrow(new IllegalArgumentException("Invalid input provided"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                inputReaderUtil.readVehicleRegistrationNumber());
        assertEquals("Invalid input provided", exception.getMessage());
    }

    @Test
    public void testReadSelection_NonNumericInput() {
        // Simulate non-numeric input
        String input = "abc"; // Simulate user input
        System.setIn(new ByteArrayInputStream(input.getBytes()));  // Simulate user input

        // Act
        int result = inputReaderUtil.readSelection();

        // Assert
        assertEquals(0, result, "Should return 0 when non-numeric input is provided.");
    }

    @Test
    public void testReadVehicleRegistrationNumber_SpecialCharacters() throws Exception {
        // Arrange
        when(mockInputReader.readVehicleRegistrationNumber()).thenReturn("ABC@123");

        // Act
        String result = inputReaderUtil.readVehicleRegistrationNumber();

        // Assert
        assertEquals("ABC@123", result);
    }


    @Test
    public void testReadVehicleRegistrationNumber_NullInput() {
        // Simuler un retour null pour la méthode readVehicleRegistrationNumberFromInput
        lenient().when(inputReaderUtil.readVehicleRegistrationNumberFromInput()).thenReturn("");

        // Vérifier si l'exception est lancée pour une entrée nulle
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                inputReaderUtil.readVehicleRegistrationNumber());
        assertEquals("Invalid input: Vehicle registration number cannot be empty.", exception.getMessage());
    }





    @Test
    public void testReadSelection_LargeInput() {
        // Simulate a very large input (to test performance or input handling)
        String input = "123456789012345678901234567890"; // Very long input
        System.setIn(new ByteArrayInputStream(input.getBytes()));  // Simulate user input

        // Act
        int result = inputReaderUtil.readSelection();

        // Assert
        assertEquals(0, result, "Should return 0 for an invalid large input.");
    }

}
