package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdynamic.model.User;
import vn.edu.hcmuaf.fit.webdynamic.service.UserService;

import java.io.IOException;
import java.net.URLEncoder;

public class LoginFacebookCallbackServlet {
    @WebServlet("/login-facebook-callback")
    public class LoginFacebookCallbackServlet extends HttpServlet {

        private static final String CLIENT_ID = "YOUR_FACEBOOK_APP_ID";
        private static final String CLIENT_SECRET = "YOUR_FACEBOOK_APP_SECRET";
        private static final String REDIRECT_URI = "http://localhost:8080/yourApp/login-facebook-callback";
        private UserService userService = new UserService();
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException, ServletException {

            String code = req.getParameter("code");
            if (code == null) {
                resp.sendRedirect("login?error=Facebook login failed");
                return;
            }

            // 1. Lấy access token
            String tokenUrl = "https://graph.facebook.com/v18.0/oauth/access_token"
                    + "?client_id=" + CLIENT_ID
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8")
                    + "&client_secret=" + CLIENT_SECRET
                    + "&code=" + code;

            String tokenResponse = HttpUtil.get(tokenUrl);
            String accessToken = JsonUtil.getValue(tokenResponse, "access_token");

            // 2. Lấy thông tin user
            String infoUrl = "https://graph.facebook.com/me"
                    + "?fields=id,name,email,picture.type(large)"
                    + "&access_token=" + accessToken;

            String infoResponse = HttpUtil.get(infoUrl);

            String fbId = JsonUtil.getValue(infoResponse, "id");
            String name = JsonUtil.getValue(infoResponse, "name");
            String email = JsonUtil.getValue(infoResponse, "email");
            String avatar = JsonUtil.getNestedValue(infoResponse, "picture.data.url");


            User user =  userService.loginByProvider("facebook", fbId);

            if (user == null) {
                User u = new User();
                u.setUsername("fb_" + fbId);
                u.setEmail(email != null ? email : "fb_" + fbId + "@facebook.com");
                u.setFirstName(name);
                u.setAvatar(avatar);
                u.setRole(1);
                u.setStatus(1);
                u.setProvider("facebook");
                u.setProviderId(fbId);

                userService.insertSocialUser(u);

                user =  userService.loginByProvider("facebook", fbId);
            }

            req.getSession().setAttribute("user", user);
            resp.sendRedirect("home");
        }
    }

}
