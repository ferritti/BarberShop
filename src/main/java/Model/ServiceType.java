package Model;

public class ServiceType {
    private String serviceName;
    private double price;

    public ServiceType() {
        this.serviceName = "";
        this.price = 0.0;
    }

    public ServiceType(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String nameService) {
        this.serviceName = nameService;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}