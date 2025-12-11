package vn.edu.hcmuaf.fit.webdynamic.service;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;
import vn.edu.hcmuaf.fit.webdynamic.model.User;

public class UserService {

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

        return user;
    }

    // Cập nhật mật khẩu
    public String updatePassword(int userId, String newPass) {
        String hashed = BCrypt.hashpw(newPass, BCrypt.gensalt());

        boolean ok = userDao.updatePassword(userId, hashed);
        return ok ? "Đổi mật khẩu thành công" : "Đổi mật khẩu thất bại";
    }
}



