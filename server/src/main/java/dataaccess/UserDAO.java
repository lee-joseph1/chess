package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user);
    UserData getUserByUsername(String username);
    void clear();
}
