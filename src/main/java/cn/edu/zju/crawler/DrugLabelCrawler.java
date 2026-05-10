package cn.edu.zju.crawler;

import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.dao.DrugDao;
import cn.edu.zju.dao.DrugLabelDao;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DrugLabelCrawler extends BaseCrawler {

    private static final Logger log = LoggerFactory.getLogger(DrugLabelCrawler.class);

    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]+>");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    /*
     * This URL is used to retrieve basic drug records.
     * It is kept from the original project structure.
     */
    public static final String URL_DRUG_BY_LABEL =
            "https://api.pharmgkb.org/v1/site/labelsByDrug";

    /*
     * These two URLs are kept consistent with Chen Xinrui's crawler.
     * The first one retrieves FDA drug-label summaries.
     * The second one retrieves detailed information for each specific drug label.
     */
    public static final String URL_DRUG_LABEL =
            "https://api.pharmgkb.org/v1/data/label?source=fda";

    public static final String URL_DRUG_LABEL_DETAIL =
            "https://api.pharmgkb.org/v1/data/label/%s?view=base";

    private DrugDao drugDao = new DrugDao();
    private DrugLabelDao drugLabelDao = new DrugLabelDao();

    public void doCrawlerDrug() {
        String content = this.getURLContent(URL_DRUG_BY_LABEL);

        if (content == null || content.trim().isEmpty()) {
            log.info("Failed to fetch drug list.");
            return;
        }

        Gson gson = new Gson();
        Map drugLabels = gson.fromJson(content, Map.class);
        List data = (List) drugLabels.get("data");

        if (data == null) {
            log.info("No drug data found.");
            return;
        }

        data.stream().forEach(x -> {
            Map row = (Map) x;
            Map drug = (Map) row.get("drug");

            if (drug == null) {
                return;
            }

            String id = asString(drug.get("id"));
            String name = asString(drug.get("name"));
            String objCls = asString(drug.get("objCls"));
            String drugUrl = asString(row.get("drugUrl"));
            boolean biomarker = asBoolean(row.get("biomarker"));

            Drug drugBean = new Drug(id, name, biomarker, drugUrl, objCls);

            if (!drugDao.existsById(id)) {
                drugDao.saveDrug(drugBean);
                log.info("Saved drug: {}", id);
            } else {
                log.info("Drug {} already exists, skip", id);
            }
        });
    }

    public void doCrawlerDrugLabel() {
        String content = this.getURLContent(URL_DRUG_LABEL);

        if (content == null || content.trim().isEmpty()) {
            log.info("Failed to fetch drug label list.");
            return;
        }

        Gson gson = new Gson();
        Map result = gson.fromJson(content, Map.class);
        List data = (List) result.get("data");

        if (data == null) {
            log.info("No drug label data found.");
            return;
        }

        data.stream().forEach(x -> {
            Map labelSummary = (Map) x;
            String labelId = asString(labelSummary.get("id"));

            if (labelId == null || labelId.trim().isEmpty()) {
                return;
            }

            String detailUrl = String.format(URL_DRUG_LABEL_DETAIL, labelId);
            String detailContent = this.getURLContent(detailUrl);

            if (detailContent == null || detailContent.trim().isEmpty()) {
                log.info("Failed to fetch label detail: {}", labelId);
                return;
            }

            Map detailResult = gson.fromJson(detailContent, Map.class);
            Map label = (Map) detailResult.get("data");

            if (label == null) {
                log.info("No detail data for label: {}", labelId);
                return;
            }

            DrugLabel drugLabelBean = buildDrugLabel(label, gson);

            if (!drugLabelDao.existsById(drugLabelBean.getId())) {
                drugLabelDao.saveDrugLabel(drugLabelBean);
                log.info("Saved drug label: {}", drugLabelBean.getId());
            } else {
                drugLabelDao.updateExtendedFields(drugLabelBean);
                log.info("Updated extended fields for drug label: {}", drugLabelBean.getId());
            }
        });
    }

    private DrugLabel buildDrugLabel(Map label, Gson gson) {
        String id = asString(label.get("id"));

        String name = asString(label.get("name"));
        if (name == null || name.trim().isEmpty()) {
            name = id;
        }

        String objCls = asString(label.get("objCls"));
        boolean alternateDrugAvailable = asBoolean(label.get("alternateDrugAvailable"));
        boolean dosingInformation = asBoolean(label.get("dosingInformation"));

        String prescribingMarkdown = getMarkdownHtml(label, "prescribingMarkdown");
        String source = asString(label.get("source"));
        String textMarkdown = getMarkdownHtml(label, "textMarkdown");
        String summaryMarkdown = getMarkdownHtml(label, "summaryMarkdown");

        String raw = gson.toJson(label);
        String drugId = getFirstRelatedChemicalId(label);

        String summaryText = getMarkdownText(label, "summaryMarkdown");
        String prescribingText = getMarkdownText(label, "prescribingMarkdown");
        String labelText = getMarkdownText(label, "textMarkdown");

        /*
         * This rule is kept consistent with Chen Xinrui's implementation:
         * efficacy_summary is populated using the cleaned summary text first;
         * if summary text is unavailable, cleaned label text is used as fallback.
         */
        String efficacySummary = firstNonBlank(summaryText, labelText);

        /*
         * The keyword lists and the search order are kept consistent with
         * Chen Xinrui's implementation.
         */
        String responseWarning = buildResponseWarning(prescribingText, labelText, summaryText);

        /*
         * The alternative-drug field is only generated when PharmGKB marks
         * alternateDrugAvailable as true.
         */
        String alternativeDrug = buildAlternativeDrug(
                alternateDrugAvailable,
                prescribingText,
                summaryText,
                labelText
        );

        return new DrugLabel(
                id,
                name,
                objCls,
                alternateDrugAvailable,
                dosingInformation,
                prescribingMarkdown,
                source,
                textMarkdown,
                summaryMarkdown,
                efficacySummary,
                responseWarning,
                alternativeDrug,
                raw,
                drugId
        );
    }

    private String buildResponseWarning(String prescribingText, String labelText, String summaryText) {
        String warningText = firstMatchingSentence(
                prescribingText,
                "warning",
                "boxed warning",
                "poor metabolizers",
                "poor metabolisers",
                "reduced effect",
                "diminished",
                "risk",
                "avoid",
                "consider"
        );

        if (warningText != null) {
            return warningText;
        }

        warningText = firstMatchingSentence(
                labelText,
                "warning",
                "boxed warning",
                "poor metabolizers",
                "poor metabolisers",
                "reduced effect",
                "diminished",
                "risk",
                "avoid"
        );

        if (warningText != null) {
            return warningText;
        }

        return firstMatchingSentence(
                summaryText,
                "warning",
                "poor metabolizers",
                "poor metabolisers",
                "reduced effect",
                "diminished",
                "risk",
                "avoid"
        );
    }

    private String buildAlternativeDrug(boolean alternateDrugAvailable,
                                        String prescribingText,
                                        String summaryText,
                                        String labelText) {
        if (!alternateDrugAvailable) {
            return null;
        }

        String alternativeDrugText = firstMatchingSentence(
                prescribingText,
                "another",
                "alternative",
                "consider",
                "avoid"
        );

        if (alternativeDrugText != null) {
            return alternativeDrugText;
        }

        alternativeDrugText = firstMatchingSentence(
                summaryText,
                "another",
                "alternative",
                "consider",
                "avoid"
        );

        if (alternativeDrugText != null) {
            return alternativeDrugText;
        }

        return firstMatchingSentence(
                labelText,
                "another",
                "alternative",
                "consider",
                "avoid"
        );
    }

    private String getMarkdownHtml(Map label, String key) {
        Object markdown = label.get(key);

        if (!(markdown instanceof Map)) {
            return "";
        }

        Object html = ((Map) markdown).get("html");

        if (!(html instanceof String)) {
            return "";
        }

        return (String) html;
    }

    private String getMarkdownText(Map label, String key) {
        String html = getMarkdownHtml(label, key);

        if (html == null || html.trim().isEmpty()) {
            return null;
        }

        return normalizeText(html);
    }

    private String normalizeText(String html) {
        String text = html
                .replaceAll("(?i)<br\\s*/?>", " ")
                .replace("&nbsp;", " ")
                .replace("&#160;", " ")
                .replace("&quot;", "\"")
                .replace("&#34;", "\"")
                .replace("&#39;", "'")
                .replace("&apos;", "'")
                .replace("&amp;", "&");

        text = HTML_TAG_PATTERN.matcher(text).replaceAll(" ");
        text = WHITESPACE_PATTERN.matcher(text).replaceAll(" ");
        text = text.replace('\u00A0', ' ').trim();

        return text.isEmpty() ? null : text;
    }

    private String firstMatchingSentence(String text, String... keywords) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        String[] sentences = text.split("(?<=[.!?])\\s+");

        for (String sentence : sentences) {
            String lowerCaseSentence = sentence.toLowerCase();

            for (String keyword : keywords) {
                if (lowerCaseSentence.contains(keyword.toLowerCase())) {
                    return sentence.trim();
                }
            }
        }

        return null;
    }

    private String firstNonBlank(String... candidates) {
        for (String candidate : candidates) {
            if (candidate != null && !candidate.trim().isEmpty()) {
                return candidate;
            }
        }

        return null;
    }

    private String getFirstRelatedChemicalId(Map label) {
        Object relatedChemicalsObj = label.get("relatedChemicals");

        if (!(relatedChemicalsObj instanceof List)) {
            return null;
        }

        List relatedChemicals = (List) relatedChemicalsObj;

        if (relatedChemicals.isEmpty()) {
            return null;
        }

        Object first = relatedChemicals.get(0);

        if (!(first instanceof Map)) {
            return null;
        }

        return asString(((Map) first).get("id"));
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private boolean asBoolean(Object value) {
        return value instanceof Boolean && (Boolean) value;
    }
}