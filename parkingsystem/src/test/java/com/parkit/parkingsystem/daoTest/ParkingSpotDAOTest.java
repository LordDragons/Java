package com.parkit.parkingsystem.daoTest;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingSpotDAOTest {

    @InjectMocks
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(dataBaseConfig.getConnection()).thenReturn(connection);
    }

    @Test
    void testGetNextAvailableSlot() throws Exception {
        when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        int slot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(1, slot);
        // Vérifiez la fermeture des ressources
        verify(dataBaseConfig).closeResultSet(resultSet);
        verify(dataBaseConfig).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    void testGetNextAvailableSlot_NoResult() throws Exception {
        // Scénario où aucune place n'est trouvée
        when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Aucune place disponible

        int slot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(-1, slot); // -1 devrait être retourné si aucune place n'est disponible
        verify(dataBaseConfig).closeResultSet(resultSet);
        verify(dataBaseConfig).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    void testUpdateParking() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean success = parkingSpotDAO.updateParking(parkingSpot);

        assertTrue(success); // Le résultat attendu est vrai
        verify(dataBaseConfig).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    void testUpdateParking_Failure() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0); // Simule un échec de mise à jour

        boolean success = parkingSpotDAO.updateParking(parkingSpot);

        assertFalse(success); // Le résultat attendu est faux en cas d'échec
        verify(dataBaseConfig).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    void testGetNextAvailableSlot_Exception() throws Exception {
        // Simule une erreur de base de données en lançant une SQLException
        when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenThrow(new SQLException("Database error"));

        int slot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(-1, slot); // La méthode doit retourner -1 en cas d'exception
        verify(dataBaseConfig).closeConnection(connection); // Vérification de la fermeture de la connexion
    }


    @Test
    void testUpdateParking_Exception() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        // Lancer une exception vérifiée (SQLException dans ce cas)
        when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenThrow(new SQLException("Database error"));

        boolean success = parkingSpotDAO.updateParking(parkingSpot);

        assertFalse(success); // La mise à jour devrait échouer en cas d'exception
        verify(dataBaseConfig).closeConnection(connection);
    }

}
