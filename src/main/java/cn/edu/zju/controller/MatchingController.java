package cn.edu.zju.controller;

import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.Sample;
import cn.edu.zju.bean.User;
import cn.edu.zju.dao.AnnovarDao;
import cn.edu.zju.dao.DrugLabelDao;
import cn.edu.zju.dao.SampleDao;
import cn.edu.zju.servlet.DispatchServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MatchingController {

    private static final Logger log = LoggerFactory.getLogger(MatchingController.class);

    private final SampleDao sampleDao = new SampleDao();
    private final AnnovarDao annovarDao = new AnnovarDao();
    private final DrugLabelDao drugLabelDao = new DrugLabelDao();

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerPostMapping("/upload", this::uploadAnnovarOutput);
        dispatcher.registerGetMapping("/matchingIndex", this::matchingIndex);
        dispatcher.registerGetMapping("/matching", this::matching);
        dispatcher.registerGetMapping("/samples", this::samples);
    }

    private User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object user = session.getAttribute("user");

        if (user instanceof User) {
            return (User) user;
        }

        return null;
    }

    private boolean requireLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User loginUser = getLoginUser(request);

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }

    public void matchingIndex(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (!requireLogin(request, response)) {
            return;
        }

        request.getRequestDispatcher("/views/matching_index.jsp").forward(request, response);
    }

    public void samples(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (!requireLogin(request, response)) {
            return;
        }

        User loginUser = getLoginUser(request);
        List<Sample> samples = sampleDao.findByUploadedBy(loginUser.getUsername());

        request.setAttribute("samples", samples);
        request.getRequestDispatcher("/views/samples.jsp").forward(request, response);
    }

    public void matching(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (!requireLogin(request, response)) {
            return;
        }

        User loginUser = getLoginUser(request);

        String sampleIdParameter = request.getParameter("sampleId");

        if (sampleIdParameter == null || sampleIdParameter.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/samples");
            return;
        }

        int sampleId;

        try {
            sampleId = Integer.parseInt(sampleIdParameter);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/samples");
            return;
        }

        Sample sample = sampleDao.findById(sampleId);

        if (sample == null) {
            response.sendRedirect(request.getContextPath() + "/samples");
            return;
        }

        if (sample.getUploadedBy() == null ||
                !sample.getUploadedBy().equals(loginUser.getUsername())) {
            response.sendRedirect(request.getContextPath() + "/samples");
            return;
        }

        List<String> refGenes = annovarDao.getRefGenes(sampleId);
        List<DrugLabel> drugLabels = drugLabelDao.findAll();
        List<DrugLabel> matched = doMatch(refGenes, drugLabels);

        request.setAttribute("matched", matched);
        request.setAttribute("sample", sample);
        request.getRequestDispatcher("/views/matching_index_search.jsp").forward(request, response);
    }

    private List<DrugLabel> doMatch(List<String> refGenes, List<DrugLabel> drugLabels) {
        List<DrugLabel> matchedLabels = new ArrayList<>();

        for (DrugLabel drugLabel : drugLabels) {
            String summary = drugLabel.getSummaryMarkdown();

            if (summary == null || summary.isBlank()) {
                continue;
            }

            String summaryLower = summary.toLowerCase();
            boolean matched = false;

            for (String gene : refGenes) {
                if (gene == null || gene.isBlank()) {
                    continue;
                }

                if (summaryLower.contains(gene.toLowerCase())) {
                    matched = true;
                    break;
                }
            }

            if (matched) {
                matchedLabels.add(drugLabel);
            }
        }

        return matchedLabels;
    }

    public void uploadAnnovarOutput(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (!requireLogin(request, response)) {
            return;
        }

        User loginUser = getLoginUser(request);

        Part requestPart = request.getPart("annovar");

        if (requestPart == null || requestPart.getSize() == 0) {
            request.setAttribute("validateError", "TSV file can not be blank.");
            request.getRequestDispatcher("/views/matching_index_error.jsp").forward(request, response);
            return;
        }

        String fileName = requestPart.getSubmittedFileName();

        if (fileName == null || !fileName.toLowerCase().endsWith(".tsv")) {
            request.setAttribute("validateError", "Please upload a .tsv file.");
            request.getRequestDispatcher("/views/matching_index_error.jsp").forward(request, response);
            return;
        }

        InputStream inputStream = requestPart.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        String content = new String(bytes, StandardCharsets.UTF_8);

        int sampleId = sampleDao.save(loginUser.getUsername());

        try {
            annovarDao.save(sampleId, content);
        } catch (Exception e) {
            log.error("Failed to save TSV mutation file", e);
            request.setAttribute("validateError",
                    "TSV file is invalid. Please check whether it contains the required columns: Chr, Start, End, Ref, Alt, Gene.refGene.");
            request.getRequestDispatcher("/views/matching_index_error.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/matching?sampleId=" + sampleId);
    }
}