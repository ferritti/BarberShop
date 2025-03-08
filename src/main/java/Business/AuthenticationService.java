package Business;

public class AuthenticationService {
    private static final String barberCode = "I-AM-A-BARBER";

    public static boolean checkBarberCode(String code) {
        return barberCode.equals(code);
    }
}
