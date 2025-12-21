package vn.edu.hcmuaf.fit.webdynamic.service;


import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserInfoDao;

import vn.edu.hcmuaf.fit.webdynamic.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    private UserDao userDAO = UserDao.getInstance();

    // Đăng ký (hash password)
    public boolean register(User user) {
        String hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hash);
        return userDAO.register(user);
    }

    // Đăng nhập (check BCrypt)
    public User login(String input, String password) {
        User user = userDAO.findByInput(input);
        if (user == null) return null;

        if (!BCrypt.checkpw(password, user.getPassword())) {
            return null;
        }
        return user;
    }
    public UserService() {
        this.userDao = new UserDao();
    }

//
    public Optional<User> getUserProfileById(int id) {
        return userDao.findById(id);
    }

    // Cập nhật mật khẩu
    public String updatePassword(int userId, String newPass) {
        String hashed = BCrypt.hashpw(newPass, BCrypt.gensalt());
        boolean ok = userDAO.updatePassword(userId, hashed);
        return ok ? "Đổi mật khẩu thành công" : "Đổi mật khẩu thất bại";
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



