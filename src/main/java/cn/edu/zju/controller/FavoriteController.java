package cn.edu.zju.controller;

import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.Favorite;
import cn.edu.zju.bean.User;
import cn.edu.zju.dao.FavoriteDao;
import cn.edu.zju.servlet.DispatchServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FavoriteController {

    private static final Logger log = LoggerFactory.getLogger(FavoriteController.class);

    private final FavoriteDao favoriteDao = new FavoriteDao();

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerPostMapping("/favorites/add", this::addFavorite);
        dispatcher.registerPostMapping("/favorites/remove", this::removeFavorite);
        dispatcher.registerGetMapping("/favorites", this::listFavorites);
    }

    public void addFavorite(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"not logged in\"}");
            return;
        }

        String type = clean(req.getParameter("resourceType"));
        String resourceId = clean(req.getParameter("resourceId"));

        if (type.isEmpty() || resourceId.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"missing params\"}");
            return;
        }

        try {
            Favorite f = new Favorite();
            f.setUserId(user.getId());
            f.setResourceType(type);
            f.setResourceId(resourceId);

            favoriteDao.save(f);

            resp.getWriter().write("{\"ok\":true}");

        } catch (SQLException e) {
            log.error("Failed to add favorite", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"database error\"}");
        }
    }

    public void removeFavorite(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"not logged in\"}");
            return;
        }

        String type = clean(req.getParameter("resourceType"));
        String resourceId = clean(req.getParameter("resourceId"));

        if (type.isEmpty() || resourceId.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"missing params\"}");
            return;
        }

        try {
            favoriteDao.deleteByUserAndResource(user.getId(), type, resourceId);
            resp.getWriter().write("{\"ok\":true}");

        } catch (SQLException e) {
            log.error("Failed to remove favorite", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"database error\"}");
        }
    }

    public void listFavorites(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            List<Drug> favoriteDrugs = favoriteDao.findFavoriteDrugsByUserId(user.getId());
            req.setAttribute("drugs", favoriteDrugs);
            req.getRequestDispatcher("/views/favorites.jsp").forward(req, resp);

        } catch (SQLException e) {
            log.error("Failed to list favorites", e);
            req.setAttribute("error", "Failed to load favorite drugs.");
            req.getRequestDispatcher("/views/favorites.jsp").forward(req, resp);
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}