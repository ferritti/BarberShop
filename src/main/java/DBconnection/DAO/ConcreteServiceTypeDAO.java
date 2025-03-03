package DBconnection.DAO;

import DBconnection.Database.DBManager;
import Model.ServiceType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConcreteServiceTypeDAO implements ServiceTypeDAO{
    private DBManager dbManager = DBManager.getInstance();

    public boolean addServiceType(ServiceType serviceType) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Service_types(service_name, price) VALUES (?, ?)"
            );
            stmt.setString(1, serviceType.getServiceName());
            stmt.setDouble(2, serviceType.getPrice());
            int row = stmt.executeUpdate();
            stmt.close();
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeServiceType(ServiceType serviceType) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM Service_type WHERE service_name = ?"
            );
            stmt.setString(1, serviceType.getServiceName());

            int row = stmt.executeUpdate();
            stmt.close();
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<ServiceType> getAllServiceTypes() {
        List<ServiceType> serviceTypes = new ArrayList<>();
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Service_ype");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ServiceType serviceType = new ServiceType(
                        rs.getString("TYPE"),
                        rs.getDouble("PRICE"));
                serviceTypes.add(serviceType);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceTypes;
    }

}
