package cn.edu.zju.controller;

import cn.edu.zju.bean.User;
import cn.edu.zju.dao.UserDao;
import cn.edu.zju.servlet.DispatchServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AuthController {

    private final UserDao userDao = new UserDao();

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerPostMapping("/register", this::registerUser);
        dispatcher.registerPostMapping("/login", this::loginUser);
        dispatcher.registerGetMapping("/logout", this::logoutUser);
    }

    public void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));
        String authorization = trim(request.getParameter("authorization"));

        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        try {
            if (userDao.exists(username)) {
                request.setAttribute("error", "This username already exists.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(UserDao.hashPassword(password));
            user.setAuthorization(authorization);

            userDao.save(user);

            response.sendRedirect(request.getContextPath() + "/views/signin.jsp?registered=1");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Registration failed. Please check the database connection or users table.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }

    public void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));

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

            response.sendRedirect(request.getContextPath() + "/");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Login failed. Please check the database connection.");
            request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
        }
    }

    public void logoutUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/views/signin.jsp");
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}