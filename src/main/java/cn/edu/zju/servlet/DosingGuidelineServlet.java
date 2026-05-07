// java
package cn.edu.zju.servlet;

import cn.edu.zju.dao.DosingGuidelineDao; // 替换为你的 DAO
import cn.edu.zju.bean.DosingGuideline;   // 替换为你的实体
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dosing_guideline")
public class DosingGuidelineServlet extends HttpServlet {
    private final DosingGuidelineDao dao = new DosingGuidelineDao(); // 或通过依赖注入/工厂获取

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<DosingGuideline> list = dao.findAll(); // 确保返回非空集合
        req.setAttribute("dosingGuidelines", list);
        req.getRequestDispatcher("/views/dosing_guideline.jsp").forward(req, resp);
    }
}
