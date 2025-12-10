package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.UserInfoDao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;

import java.util.Optional;

public class UserService {
    private final UserInfoDao userDao;

    public UserService() {
        this.userDao = new UserInfoDao();
    }

    public Optional<User> getUserProfileById(int id) {
        return userDao.findById(id);
    }

    public Optional<User> getUserProfileByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
