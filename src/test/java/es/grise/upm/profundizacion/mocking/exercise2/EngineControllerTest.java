package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class EngineControllerTest {

    private Logger mockLogger;
    private Speedometer mockSpeedometer;
    private Gearbox mockGearbox;
    private Time mockTime;
    private EngineController engineController;

    @BeforeEach
    public void setup() {
        // Mock dependencies
        mockLogger = mock(Logger.class);
        mockSpeedometer = mock(Speedometer.class);
        mockGearbox = mock(Gearbox.class);
        mockTime = mock(Time.class);

        // Initialize the system under test (SUT)
        engineController = new EngineController(mockLogger, mockSpeedometer, mockGearbox, mockTime);
    }

    // Test methods will go here
    @Test
    public void testRecordGearLogFormat() {
        // Arrange
        GearValues newGear = GearValues.FIRST;
        // Mock the timestamp
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1234567890L));

        String expectedLogMessage = "1970-01-15 07:56:07 Gear changed to FIRST";

        // Act
        engineController.recordGear(newGear);

        // Assert
        verify(mockLogger).log(expectedLogMessage); // Verify correct log message
    }
    
    @Test
    public void testGetInstantaneousSpeed() {
        // Arrange
        when(mockSpeedometer.getSpeed()).thenReturn(10.0, 20.0, 30.0); // Mock speed values

        // Act
        double averageSpeed = engineController.getInstantaneousSpeed();

        // Assert
        assertEquals(20.0, averageSpeed, 0.01); // Verify the average speed is correct
    }

    @Test
    public void testAdjustGearCallsGetInstantaneousSpeedThreeTimes() {
        // Arrange
        when(mockSpeedometer.getSpeed()).thenReturn(10.0); // Mock speed
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1234567890L)); // Mock time

        // Act
        engineController.adjustGear();

        // Assert
        verify(mockSpeedometer, times(3)).getSpeed(); // Verify getSpeed() was called 3 times
    }

    
    
    @Test
    public void testAdjustGearLogsNewGear() {
        // Arrange
        when(mockSpeedometer.getSpeed()).thenReturn(10.0); // Mock speed values
        // Mock the timestamp
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1234567890L));


        // Adjust expected log message for UTC timezone
        String expectedLogMessage = "1970-01-15 07:56:07 Gear changed to FIRST";

        // Act
        engineController.adjustGear();

        // Assert
        verify(mockLogger).log(expectedLogMessage); // Verify correct log message
    }
    
    
    @Test
    public void testAdjustGearSetsCorrectGear() {
        // Arrange
        when(mockSpeedometer.getSpeed()).thenReturn(10.0); // Mock speed
        when(mockTime.getCurrentTime()).thenReturn(new Timestamp(1234567890L)); // Mock time

        // Act
        engineController.adjustGear();

        // Assert
        verify(mockGearbox).setGear(GearValues.FIRST); // Verify correct gear is set
    }


}

