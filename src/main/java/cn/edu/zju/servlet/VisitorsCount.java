package cn.edu.zju.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/visitors")
public class VisitorsCount extends HttpServlet {

    private static int count = 0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        count++;  // 每访问一次就 +1

        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().println("Visitor count: " + count);
    }
}
