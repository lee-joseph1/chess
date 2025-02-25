package dataaccess;

import model.UserData;
import java.util.HashMap;

public interface UserDAO {

    void createUser(UserData user);
    UserData getUserByUsername(String username);
    void clear();
}
