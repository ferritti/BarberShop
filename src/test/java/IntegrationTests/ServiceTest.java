package IntegrationTests;

import Business.*;
import Persistence.DAO.*;
import Model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private ServicesService servicesService;
    private ConcreteServiceTypeDAO serviceTypeDAO;

    private final String serviceName = "Hair";
    private final double servicePrice = 25.0;

    @BeforeEach
    void setUp() {
        servicesService = new ServicesService();
        serviceTypeDAO = new ConcreteServiceTypeDAO();
    }

    @AfterEach
    void tearDown() {
        ServiceType serviceToDelete = serviceTypeDAO.getAllServiceTypes().stream()
                .filter(s -> s.getServiceName().equals(serviceName))
                .findFirst()
                .orElse(null);

        if (serviceToDelete != null) {
            servicesService.deleteService(serviceToDelete);
        }
    }

    @Test
    void testBarberCreateService() {
        boolean isServiceAdded = servicesService.addService(serviceName, servicePrice);
        assertTrue(isServiceAdded);

        ServiceType service = serviceTypeDAO.getAllServiceTypes().stream()
                .filter(s -> s.getServiceName().equals(serviceName))
                .findFirst()
                .orElse(null);

        assertNotNull(service);
        assertEquals(serviceName, service.getServiceName());
        assertEquals(servicePrice, service.getPrice());
    }

    @Test
    void testDeleteService() {
        servicesService.addService(serviceName, servicePrice);
        ServiceType serviceToDelete = serviceTypeDAO.getAllServiceTypes().stream()
                .filter(s -> s.getServiceName().equals(serviceName))
                .findFirst()
                .orElse(null);

        assertNotNull(serviceToDelete);
        boolean isServiceDeleted = servicesService.deleteService(serviceToDelete);
        assertTrue(isServiceDeleted);

        ServiceType deletedService = serviceTypeDAO.getAllServiceTypes().stream()
                .filter(s -> s.getServiceName().equals(serviceName))
                .findFirst()
                .orElse(null);

        assertNull(deletedService);
    }
}