package Model;

public class Barber extends User {
    public Barber(int idUser, String name, String surname, String email, String password, String phone) {
        super(idUser, name, surname, email, password, phone, UserType.BARBER);
    }
}
