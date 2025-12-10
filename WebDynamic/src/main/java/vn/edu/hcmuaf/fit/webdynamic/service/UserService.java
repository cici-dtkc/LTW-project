package vn.edu.hcmuaf.fit.webdynamic.service;

import vn.edu.hcmuaf.fit.webdynamic.dao.UserDao;

public class UserService {
    private UserDao userDao= new UserDao();

    public String changePassword(int userId, String oldPass, String newPass) {
        if (!userDao.checkOldPassword(userId, oldPass)) {
            return "Mật khẩu cũ không đúng!";
        }

        boolean updated = userDao.updatePassword(userId, newPass);

        return updated ? "Đổi mật khẩu thành công" : "Đổi mật khẩu thất bại";
    }

}
