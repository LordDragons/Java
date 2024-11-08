package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class InteractiveShellTest {

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private TicketDAO ticketDAO;

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private InteractiveShell interactiveShell; // Mocked dependencies will be injected here

    InteractiveShellTest(ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks
    }

    @Test
    void testLoadInterfaceNewVehicle() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1, 3); // 1: New vehicle, 3: Exit

        interactiveShell.loadInterface();

        verify(parkingService, times(1)).processIncomingVehicle();
    }

    @Test
    void testLoadInterfaceExitingVehicle() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(2, 3); // 2: Exiting vehicle, 3: Exit

        interactiveShell.loadInterface();

        verify(parkingService, times(1)).processExitingVehicle();
    }

    @Test
    void testLoadInterfaceExitOption() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(3); // Exit directly

        interactiveShell.loadInterface();

        // Verify no other method was called
        verify(parkingService, never()).processIncomingVehicle();
        verify(parkingService, never()).processExitingVehicle();
    }

    @Test
    void testLoadInterfaceUnsupportedOption() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(4, 3); // 4: Unsupported option, 3: Exit

        interactiveShell.loadInterface();

        // Verify no other method was called
        verify(parkingService, never()).processIncomingVehicle();
        verify(parkingService, never()).processExitingVehicle();
    }

    public ParkingSpotDAO getParkingSpotDAO() {
        return parkingSpotDAO;
    }

    public void setParkingSpotDAO(ParkingSpotDAO parkingSpotDAO) {
        this.parkingSpotDAO = parkingSpotDAO;
    }

    public TicketDAO getTicketDAO() {
        return ticketDAO;
    }

    public void setTicketDAO(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }
}
