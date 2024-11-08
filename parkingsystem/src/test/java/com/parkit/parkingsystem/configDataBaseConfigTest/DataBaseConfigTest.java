package com.parkit.parkingsystem.configDataBaseConfigTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import com.parkit.parkingsystem.config.DataBaseConfig;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DataBaseConfigTest {

    private DataBaseConfig dataBaseConfig;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;

    @BeforeEach
    void setUp() {
        dataBaseConfig = new DataBaseConfig();
        connectionMock = Mockito.mock(Connection.class);
        preparedStatementMock = Mockito.mock(PreparedStatement.class);
        resultSetMock = Mockito.mock(ResultSet.class);
    }

    @Test
    void testCloseConnection() throws SQLException {
        dataBaseConfig.closeConnection(connectionMock);
        verify(connectionMock, times(1)).close();
    }

    @Test
    void testClosePreparedStatement() throws SQLException {
        dataBaseConfig.closePreparedStatement(preparedStatementMock);
        verify(preparedStatementMock, times(1)).close();
    }

    @Test
    void testCloseResultSet() throws SQLException {
        dataBaseConfig.closeResultSet(resultSetMock);
        verify(resultSetMock, times(1)).close();
    }

    void testGetConnection() {
        try {
            Connection connection = dataBaseConfig.getConnection();
            assertNotNull(connection, "Connection should not be null");
            connection.close(); // Close the connection after the test
        } catch (Exception e) {
            fail("Exception should not occur during connection: " + e.getMessage());
        }
    }
}
