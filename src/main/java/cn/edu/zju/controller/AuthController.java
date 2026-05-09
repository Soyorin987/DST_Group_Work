package cn.edu.zju.controller;

import cn.edu.zju.bean.User;
import cn.edu.zju.dao.UserDao;
import cn.edu.zju.servlet.DispatchServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserDao userDao = new UserDao();

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerGetMapping("/login", this::showLoginPage);
        dispatcher.registerGetMapping("/register", this::showRegisterPage);
        dispatcher.registerGetMapping("/logout", this::logout);

        dispatcher.registerPostMapping("/login", this::login);
        dispatcher.registerPostMapping("/register", this::registerUser);
    }

    public void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
    }

    public void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    public void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = clean(request.getParameter("username"));
        String password = clean(request.getParameter("password"));
        String authorization = clean(request.getParameter("authorization"));

        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        try {
            if (userDao.exists(username)) {
                request.setAttribute("error", "This username already exists. Please choose another username.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(UserDao.hashPassword(password));
            user.setAuthorization(authorization);

            userDao.save(user);

            response.sendRedirect(request.getContextPath() + "/login?registered=1");

        } catch (SQLException e) {
            log.error("Registration failed", e);
            request.setAttribute("error", "Registration failed. Please check the database connection and users table.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }

    public void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = clean(request.getParameter("username"));
        String password = clean(request.getParameter("password"));

        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
            return;
        }

        try {
            boolean valid = userDao.verifyPassword(username, password);

            if (!valid) {
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
                return;
            }

            User user = userDao.findByUsername(username);

            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("username", user.getUsername());

            response.sendRedirect(request.getContextPath() + "/");

        } catch (SQLException e) {
            log.error("Login failed", e);
            request.setAttribute("error", "Login failed. Please check the database connection.");
            request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}