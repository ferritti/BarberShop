package Business;

import DBconnection.DAO.ConcreteServiceTypeDAO;
import DBconnection.DAO.ServiceTypeDAO;
import Model.ServiceType;

import java.util.List;

public class ServicesService {
    private ServiceTypeDAO serviceTypeDAO;

    public ServicesService() {
        this.serviceTypeDAO = new ConcreteServiceTypeDAO();
    }

    public ServicesService(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public boolean areEmptyFields(String name, String price) {
        return name.trim().isEmpty() || price.trim().isEmpty();
    }

    public String validatePrice(String priceText) {
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                return "Price must be greater than 0.";
            }
        } catch (NumberFormatException e) {
            return "Price must be a valid number.";
        }
        return null;
    }

    public boolean addService(String name, double price) {
        ServiceType serviceType = new ServiceType(name, price);
        return serviceTypeDAO.addServiceType(serviceType);
    }

    public List<ServiceType> getService() {
        return serviceTypeDAO.getAllServiceTypes();
    }

    public boolean deleteService(ServiceType serviceType) {
        return serviceTypeDAO.removeServiceType(serviceType);
    }
}
