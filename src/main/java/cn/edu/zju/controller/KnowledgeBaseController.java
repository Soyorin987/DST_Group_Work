package cn.edu.zju.controller;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.User;
import cn.edu.zju.dao.DosingGuidelineDao;
import cn.edu.zju.dao.DrugDao;
import cn.edu.zju.dao.DrugLabelDao;
import cn.edu.zju.dao.FavoriteDao;
import cn.edu.zju.servlet.DispatchServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KnowledgeBaseController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    private final DrugDao drugDao = new DrugDao();
    private final DrugLabelDao drugLabelDao = new DrugLabelDao();
    private final DosingGuidelineDao dosingGuidelineDao = new DosingGuidelineDao();
    private final FavoriteDao favoriteDao = new FavoriteDao();

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerGetMapping("/drugs", this::drugs);
        dispatcher.registerGetMapping("/drugLabels", this::drugLabels);
        dispatcher.registerGetMapping("/dosingGuideline", this::dosingGuideline);
    }

    public void drugs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = trim(request.getParameter("keyword"));

        List<Drug> drugs;
        if (!keyword.isEmpty()) {
            drugs = drugDao.findByKeyword(keyword);
        } else {
            drugs = drugDao.findAll();
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            try {
                Set<String> favoriteDrugIds = new HashSet<>(
                        favoriteDao.findFavoriteResourceIds(user.getId(), "drug")
                );

                for (Drug drug : drugs) {
                    drug.setFavorited(favoriteDrugIds.contains(drug.getId()));
                }

            } catch (SQLException e) {
                log.error("Failed to load favorite status for drugs", e);
            }
        }

        request.setAttribute("keyword", keyword);
        request.setAttribute("drugs", drugs);
        request.getRequestDispatcher("/views/drugs.jsp").forward(request, response);
    }

    public void drugLabels(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = trim(request.getParameter("keyword"));

        List<DrugLabel> drugLabels;
        if (!keyword.isEmpty()) {
            drugLabels = drugLabelDao.findByKeyword(keyword);
        } else {
            drugLabels = drugLabelDao.findAll();
        }

        request.setAttribute("keyword", keyword);
        request.setAttribute("drugLabels", drugLabels);
        request.getRequestDispatcher("/views/drug_labels.jsp").forward(request, response);
    }

    public void dosingGuideline(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = trim(request.getParameter("keyword"));

        List<DosingGuideline> dosingGuidelines;
        if (!keyword.isEmpty()) {
            dosingGuidelines = dosingGuidelineDao.findByKeyword(keyword);
        } else {
            dosingGuidelines = dosingGuidelineDao.findAll();
        }

        request.setAttribute("keyword", keyword);
        request.setAttribute("dosingGuidelines", dosingGuidelines);
        request.getRequestDispatcher("/views/dosing_guideline.jsp").forward(request, response);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}