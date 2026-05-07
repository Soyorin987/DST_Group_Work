// language: java
package cn.edu.zju.servlet;

import cn.edu.zju.bean.Drug;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple servlet that prepares the current user's favorited drugs and forwards to /views/favorites.jsp.
 * Replace the TODO area with calls to your existing DAO/service to load actual favorites.
 */
@WebServlet("/favorites")
public class FavoriteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从 session 获取当前用户 id（根据你项目的登录实现调整）
        String userId = null;
        Object userObj = req.getSession().getAttribute("currentUser");
        if (userObj != null) {
            try {
                // 假设 userObj 有 getId() 方法；若不同请修改为实际属性
                userId = (String) userObj.getClass().getMethod("getId").invoke(userObj);
            } catch (Exception ignored) { }
        }

        List<Drug> favorites = new ArrayList<>();

        // TODO: 用你项目中已有的 DAO/Service 加载该 userId 的收藏 drug 列表
        // 示例（如果存在）：
        // cn.edu.zju.dao.FavoriteDao favDao = new cn.edu.zju.dao.FavoriteDao();
        // favorites = favDao.getFavoritedDrugsByUserId(userId);
        //
        // 若没有专门 DAO，可以从 DrugDao 加载所有 drug 并根据用户收藏表过滤。

        // 将结果放入请求属性（JSP 使用 ${drugs}）
        req.setAttribute("drugs", favorites);

        // 转发到 JSP
        req.getRequestDispatcher("/views/favorites.jsp").forward(req, resp);
    }
}
