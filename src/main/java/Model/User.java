package Model;

import Authentication.HashingPassword;

public abstract class User {
    private String name;
    private String surname;
    private String email;
    private String hashedPass;
    private String phone;
    public static enum UserType {CUSTOMER, BARBER}; //potrebbe aver senso metterlo in un file a parte
    private final UserType userType;

    public User(String name, String surname,
                String email, String password, String phone, UserType userType) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        setPassword(password);
        this.phone = phone;
        this.userType = userType;
    }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getEmail() { return email; }

    public String getHashedPass() { return hashedPass; }

    public void setPassword(String password) {
        this.hashedPass = HashingPassword.hashPassword(password);
    }

    public String getPhone() { return phone; }

    public UserType getUserType() { return userType; }
}