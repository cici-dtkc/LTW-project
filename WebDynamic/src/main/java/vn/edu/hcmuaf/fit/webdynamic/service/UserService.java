package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public Optional<User> getUserProfileById(int id) {
        return userDao.findById(id);
    }

    public Optional<User> getUserProfileByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public boolean updateUser(int id, int role, int status) {
        return userDao.updateUser(id, role, status);
    }

    public boolean updateUserInfo(int id, String firstName, String lastName, String email) {
        return userDao.updateUserInfo(id, firstName, lastName, email);
    }

    public boolean updateAvatar(int id, String avatarUrl) {
        return userDao.updateAvatar(id, avatarUrl);
    }

    public boolean checkExistEmailForOtherUsers(int id, String email) {
        return userDao.checkExistEmailForOtherUsers(id, email);
    }

}
