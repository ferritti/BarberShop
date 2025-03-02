package Model;

public abstract class User {
    private int idUser;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    public static enum UserType {CUSTOMER, BARBER};
    private final UserType userType;
    private boolean isLogged;

    public User(int idUser, String name, String surname,
                String email, String password, String phone, UserType userType) {
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userType = userType;
        this.isLogged = false;
    }

    public int getIdUser() { return idUser; }

    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public boolean isLogged() { return isLogged; }

    public void setLogged(boolean logged) { isLogged = logged; }

    public User.UserType getUserType() { return userType; }
}