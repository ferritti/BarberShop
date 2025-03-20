package Model;

public class Customer extends User{
    public Customer(String name, String surname, String email, String password, String phone) {
        super(name, surname, email, password, phone, UserType.CUSTOMER);
    }
}
