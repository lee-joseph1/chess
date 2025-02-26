package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class AuthService {

    public AuthService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {}

    public void clear() {
        authDao.clear();
    }
}
