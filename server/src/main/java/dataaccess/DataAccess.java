package dataaccess;
import model.*;
import model.UserData;
import model.GameData;

public interface DataAccess {
    void createUser(UserData user) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
}
