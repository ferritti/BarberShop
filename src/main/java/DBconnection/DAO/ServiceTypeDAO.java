package DBconnection.DAO;

import Model.ServiceType;

import java.sql.SQLException;

import java.util.List;

public interface ServiceTypeDAO {
    public boolean addServiceType(ServiceType serviceType) ;
    public boolean removeServiceType(ServiceType serviceType) ;
    public List<ServiceType> getAllServiceTypes() ;
}
