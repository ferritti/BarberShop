package Business;

import Authentication.SessionManager;
import Model.User;

import java.util.HashMap;
import java.util.Map;

public class ProfileService {

    public void logout() {
        SessionManager.getInstance().resetUser();
    }

    public Map<String, String> getUserData() {
        Map<String, String> data = new HashMap<>();

        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            data.put("name", user.getName());
            data.put("surname", user.getSurname());
            data.put("email", user.getEmail());
            data.put("phone", user.getPhone());
        }

        return data;
    }
}
