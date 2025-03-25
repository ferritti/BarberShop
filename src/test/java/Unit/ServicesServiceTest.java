package Unit;

import Business.ServicesService;
import DBconnection.DAO.ServiceTypeDAO;
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
    void testAreEmptyFields() {
        // Test con nome vuoto e prezzo valido
        assertTrue(servicesService.areEmptyFields("  ", "10.0"), "Fields should be empty if name is blank");

        // Test con nome valido e prezzo vuoto
        assertTrue(servicesService.areEmptyFields("Test Service", "  "), "Fields should be empty if price is blank");

        // Test con entrambi i campi validi
        assertFalse(servicesService.areEmptyFields("Test Service", "10.0"), "Fields should not be empty if both are filled");
    }

    // Test per il metodo validatePrice
    @Test
    void testValidatePrice() {
        // Test con prezzo valido
        String validPrice = "10.0";
        String result = servicesService.validatePrice(validPrice);
        assertNull(result, "The price should be valid and return null");

        // Test con prezzo non valido (input non numerico)
        String invalidPrice = "abc";
        result = servicesService.validatePrice(invalidPrice);
        assertEquals("Price must be a valid number.", result, "Price should be invalid if not a number");

        // Test con prezzo negativo
        String negativePrice = "-5.0";
        result = servicesService.validatePrice(negativePrice);
        assertEquals("Price must be greater than 0.", result, "Price should be invalid if less than or equal to 0");
    }

    @Test
    void testAddService() {
        // Configura il comportamento del mock: quando addServiceType è chiamato, restituisce true
        when(mockServiceTypeDAO.addServiceType(any(ServiceType.class))).thenReturn(true);

        // Testa l'aggiunta di un servizio
        boolean result = servicesService.addService("Test Service", 10.0);
        assertTrue(result, "Service should be added successfully");

        // Verifica che il metodo addServiceType del DAO sia stato chiamato
        verify(mockServiceTypeDAO, times(1)).addServiceType(any(ServiceType.class));
    }

    // Test per il metodo deleteService con Mockito per il DAO
    @Test
    void testDeleteService() {
        // Crea il mock per il servizio
        ServiceType serviceType = new ServiceType("Test Service", 10.0);

        // Configura il comportamento del mock: quando removeServiceType è chiamato, restituisce true
        when(mockServiceTypeDAO.removeServiceType(serviceType)).thenReturn(true);

        // Testa l'eliminazione di un servizio
        boolean result = servicesService.deleteService(serviceType);
        assertTrue(result, "Service should be deleted successfully");

        // Verifica che il metodo removeServiceType del DAO sia stato chiamato con l'oggetto corretto
        verify(mockServiceTypeDAO, times(1)).removeServiceType(serviceType);
    }
}
