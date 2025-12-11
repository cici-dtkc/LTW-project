package vn.edu.hcmuaf.fit.webdynamic.service;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserInfoDao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;

import java.util.Optional;

public class UserService {
    private final UserInfoDao userDao;

    private UserDao userDao = UserDao.getInstance();

    // Đăng ký (hash password)
    public boolean register(User user) {
        String hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hash);
        return userDao.register(user);
    }

    // Đăng nhập (check BCrypt)
    public User login(String input, String password) {
        User user = userDao.findByInput(input);
        if (user == null) return null;

        if (!BCrypt.checkpw(password, user.getPassword())) {
            return null;
        }
    public UserService() {
        this.userDao = new UserInfoDao();
    }

        return user;
    public Optional<User> getUserProfileById(int id) {
        return userDao.findById(id);
    }

    // Cập nhật mật khẩu
    public String updatePassword(int userId, String newPass) {
        String hashed = BCrypt.hashpw(newPass, BCrypt.gensalt());

        boolean ok = userDao.updatePassword(userId, hashed);
        return ok ? "Đổi mật khẩu thành công" : "Đổi mật khẩu thất bại";
    public Optional<User> getUserProfileByUsername(String username) {
        return userDao.findByUsername(username);
    }
}



