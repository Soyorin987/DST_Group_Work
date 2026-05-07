// java
package cn.edu.zju.servlet;

import cn.edu.zju.dao.DrugLabelDao;
import cn.edu.zju.bean.DrugLabel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/drug_labels")
public class DrugLabelsServlet extends HttpServlet {
    private final DrugLabelDao dao = new DrugLabelDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<DrugLabel> list = dao.findAll();
        req.setAttribute("drugLabels", list);
        req.getRequestDispatcher("/views/drug_labels.jsp").forward(req, resp);
    }
}
