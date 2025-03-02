package Model;

public class Customer extends User{

    public Customer(int idUser, String name, String surname, String email, String password, String phone) {
        super(idUser, name, surname, email, password, phone, UserType.CUSTOMER);
    }
}
