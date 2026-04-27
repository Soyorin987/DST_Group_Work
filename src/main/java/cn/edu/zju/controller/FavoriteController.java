package cn.edu.zju.controller;

import cn.edu.zju.bean.Favorite;
import cn.edu.zju.bean.User;
import cn.edu.zju.dao.FavoriteDao;
import cn.edu.zju.servlet.DispatchServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class FavoriteController {
    private FavoriteDao favoriteDao = new FavoriteDao();

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerPostMapping("/favorites/add", this::addFavorite);
        dispatcher.registerPostMapping("/favorites/remove", this::removeFavorite);
        dispatcher.registerGetMapping("/favorites", this::listFavorites);
    }

    public void addFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"not logged in\"}");
            return;
        }
        String type = req.getParameter("resourceType");
        String idStr = req.getParameter("resourceId");
        if (type == null || idStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"missing params\"}");
            return;
        }
        long resourceId = Long.parseLong(idStr);
        try {
            if (!favoriteDao.exists(user.getId(), type, resourceId)) {
                Favorite f = new Favorite();
                f.setUserId(user.getId());
                f.setResourceType(type);
                f.setResourceId(resourceId);
                favoriteDao.save(f);
            }
            resp.setContentType("application/json");
            resp.getWriter().write("{\"ok\":true}");
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"ok\":false}");
        }
    }

    public void removeFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"not logged in\"}");
            return;
        }
        String type = req.getParameter("resourceType");
        String idStr = req.getParameter("resourceId");
        if (type == null || idStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"ok\":false,\"msg\":\"missing params\"}");
            return;
        }
        long resourceId = Long.parseLong(idStr);
        try {
            favoriteDao.deleteByUserAndResource(user.getId(), type, resourceId);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"ok\":true}");
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"ok\":false}");
        }
    }

    public void listFavorites(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 简单转发收藏页（可以根据项目需要把 favorites.jsp 加入视图）
        req.getRequestDispatcher("/views/favorites.jsp").forward(req, resp);
    }
}
