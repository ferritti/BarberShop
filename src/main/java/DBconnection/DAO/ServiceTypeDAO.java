package DBconnection.DAO;

import Model.ServiceType;

import java.util.HashMap;
import java.util.List;

public interface ServiceTypeDAO {
    public boolean addServiceType(ServiceType serviceType) ;
    public boolean removeServiceType(ServiceType serviceType) ;
    public List<ServiceType> getAllServiceTypes();
    public HashMap<String, Double> getServices();
}
