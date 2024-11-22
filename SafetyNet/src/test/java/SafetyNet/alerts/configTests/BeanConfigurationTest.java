package SafetyNet.alerts.config;

import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.SafetyNetApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SafetyNetApplication.class)
public class BeanConfigurationTest {

    @Autowired
    private Data data; // Inject the Data bean to test it

    @Test
    public void testDataBeanLoaded() {
        assertNotNull(data, "The Data bean should be loaded and not null.");

        // You can also add more checks to verify the content of the Data object
        // For example, assuming the Data class has a list of persons:
        assertFalse(data.getPersons().isEmpty(), "The persons list should not be empty.");
    }
}

