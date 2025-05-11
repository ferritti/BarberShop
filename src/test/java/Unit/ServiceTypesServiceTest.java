package Unit;

import Services.ServiceTypesService;
import Persistence.DAO.ServiceTypeDAO;
import Model.ServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTypesServiceTest {
    private ServiceTypeDAO mockServiceTypeDAO;
    private ServiceTypesService serviceTypesService;

    @BeforeEach
    void setUp() {
        mockServiceTypeDAO = mock(ServiceTypeDAO.class);
        serviceTypesService = new ServiceTypesService(mockServiceTypeDAO);
    }

    @Test
    void emptyFieldsShouldBeTrue() {
        assertTrue(serviceTypesService.areEmptyFields("  ", "10.0"));
        assertTrue(serviceTypesService.areEmptyFields("Test Service", " "));
    }

    @Test
    void filledFieldsShouldBeFalse() {
        assertFalse(serviceTypesService.areEmptyFields("Test Service", "10.0"));
    }

    @Test
    void validatePriceWorks() {
        assertNull(serviceTypesService.validatePrice("10.0"));
        assertEquals("Price must be a valid number.", serviceTypesService.validatePrice("abc"));
        assertEquals("Price must be greater than 0.", serviceTypesService.validatePrice("-5.0"));
    }

    @Test
    void addServiceAdds() {
        when(mockServiceTypeDAO.addServiceType(any(ServiceType.class))).thenReturn(true);

        assertTrue(serviceTypesService.addService("Test Service", 10.0));
        verify(mockServiceTypeDAO, times(1)).addServiceType(any(ServiceType.class));
    }

    @Test
    void deleteServiceRemoves() {
        ServiceType serviceType = new ServiceType("Test Service", 10.0);
        when(mockServiceTypeDAO.removeServiceType(serviceType)).thenReturn(true);

        assertTrue(serviceTypesService.deleteService(serviceType));
        verify(mockServiceTypeDAO, times(1)).removeServiceType(serviceType);
    }
}