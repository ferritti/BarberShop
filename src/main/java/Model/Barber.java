package Model;

public class Barber extends User {
    public Barber(String name, String surname, String email, String password, String phone) {
        super(name, surname, email, password, phone, UserType.BARBER);
    }
}
