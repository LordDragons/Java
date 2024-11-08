package com.parkit.parkingsystem.util;

public interface InputReader {
    int readSelection();
    String readVehicleRegistrationNumber() throws Exception;

    Object readVehicleRegistrationNumberFromInput();
}
