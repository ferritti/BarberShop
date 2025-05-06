package Model;

public class ServiceType {
    private String serviceName;
    private double price;

    public ServiceType(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}