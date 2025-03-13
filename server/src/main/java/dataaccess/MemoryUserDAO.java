package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO  implements UserDAO {
    private final HashMap<String, UserData> userMap = new HashMap<>();

    @Override
    public void createUser(UserData user) {
        userMap.put(user.username(), user);
    }

    @Override
    public UserData getUserByUsername(String username) {
        return userMap.get(username);
    }

    @Override
    public void clear() {
        userMap.clear();
    }

    @Override
    public boolean verifyUser(String username, String providedClearTextPassword) {
        UserData userData = getUserByUsername(username);
        if (userData != null) {
            return Objects.equals(providedClearTextPassword, userData.password());
        }
        return false;
    }
}
