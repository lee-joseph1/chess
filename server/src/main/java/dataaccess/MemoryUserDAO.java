package dataaccess;

import model.UserData;
import java.util.HashMap;

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
}
