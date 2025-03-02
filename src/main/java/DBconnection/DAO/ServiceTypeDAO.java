package DBconnection.DAO;

import Model.ServiceType;

import java.sql.SQLException;

import java.util.List;

public interface ServiceTypeDAO {
    public void addServiceType(ServiceType serviceType) throws SQLException;
    public void removeServiceType(ServiceType serviceType) throws SQLException;
    public List<ServiceType> getAllServiceTypes() throws SQLException;
}
