package Unit;

import Services.ServicesService;
import Persistence.DAO.ServiceTypeDAO;
import Model.ServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ServicesServiceTest {
    private ServiceTypeDAO mockServiceTypeDAO;
    private ServicesService servicesService;

    @BeforeEach
    void setUp() {
        mockServiceTypeDAO = mock(ServiceTypeDAO.class);
        servicesService = new ServicesService(mockServiceTypeDAO);
    }

    @Test
    void emptyFieldsShouldBeTrue() {
        assertTrue(servicesService.areEmptyFields("  ", "10.0"));
        assertTrue(servicesService.areEmptyFields("Test Service", " "));
    }

    @Test
    void filledFieldsShouldBeFalse() {
        assertFalse(servicesService.areEmptyFields("Test Service", "10.0"));
    }

    @Test
    void validatePriceWorks() {
        assertNull(servicesService.validatePrice("10.0"));
        assertEquals("Price must be a valid number.", servicesService.validatePrice("abc"));
        assertEquals("Price must be greater than 0.", servicesService.validatePrice("-5.0"));
    }

    @Test
    void addServiceAdds() {
        when(mockServiceTypeDAO.addServiceType(any(ServiceType.class))).thenReturn(true);

        assertTrue(servicesService.addService("Test Service", 10.0));
        verify(mockServiceTypeDAO, times(1)).addServiceType(any(ServiceType.class));
    }

    @Test
    void deleteServiceRemoves() {
        ServiceType serviceType = new ServiceType("Test Service", 10.0);
        when(mockServiceTypeDAO.removeServiceType(serviceType)).thenReturn(true);

        assertTrue(servicesService.deleteService(serviceType));
        verify(mockServiceTypeDAO, times(1)).removeServiceType(serviceType);
    }
}