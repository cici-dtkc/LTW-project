package vn.edu.hcmuaf.fit.webdynamic.controller.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/login-facebook")
public class LoginFacebookServlet extends HttpServlet {

    private static final String CLIENT_ID = "YOUR_FACEBOOK_APP_ID";
    private static final String CLIENT_SECRET = "YOUR_FACEBOOK_APP_SECRET";
    private static final String REDIRECT_URI = "http://localhost:8080/yourApp/login-facebook-callback";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String url = "https://www.facebook.com/v18.0/dialog/oauth"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8")
                + "&scope=email,public_profile";

        resp.sendRedirect(url);
    }
}
