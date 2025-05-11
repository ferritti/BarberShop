package IntegrationTests;

import Services.*;
import Persistence.DAO.*;
import Model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTypesTest {

    private ServiceTypesService serviceTypesService;
    private ConcreteServiceTypeDAO serviceTypeDAO;

    private final String serviceName = "Hair";
    private final double servicePrice = 25.0;

    @BeforeEach
    void setUp() {
        serviceTypesService = new ServiceTypesService();
        serviceTypeDAO = new ConcreteServiceTypeDAO();
    }

    @AfterEach
    void tearDown() {
        ServiceType serviceToDelete = serviceTypeDAO.getAllServiceTypes().stream()
                .filter(s -> s.getName().equals(serviceName))
                .findFirst()
                .orElse(null);

        if (serviceToDelete != null) {
            serviceTypesService.deleteService(serviceToDelete);
        }
    }

    @Test
    void testBarberCreateService() {
        boolean isServiceAdded = serviceTypesService.addService(serviceName, servicePrice);
        assertTrue(isServiceAdded);

        ServiceType service = serviceTypeDAO.getAllServiceTypes().stream()
                .filter(s -> s.getName().equals(serviceName))
                .findFirst()
                .orElse(null);

        assertNotNull(service);
        assertEquals(serviceName, service.getName());
        assertEquals(servicePrice, service.getPrice());
    }

    @Test
    void testBarberDeleteService() {
        serviceTypesService.addService(serviceName, servicePrice);
        ServiceType serviceToDelete = serviceTypeDAO.getAllServiceTypes().stream()
                .filter(s -> s.getName().equals(serviceName))
                .findFirst()
                .orElse(null);

        assertNotNull(serviceToDelete);
        boolean isServiceDeleted = serviceTypesService.deleteService(serviceToDelete);
        assertTrue(isServiceDeleted);

        ServiceType deletedService = serviceTypeDAO.getAllServiceTypes().stream()
                .filter(s -> s.getName().equals(serviceName))
                .findFirst()
                .orElse(null);

        assertNull(deletedService);
    }
}