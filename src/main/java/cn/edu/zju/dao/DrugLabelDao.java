package cn.edu.zju.dao;

import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrugLabelDao extends BaseDao {
    private static final Logger log = LoggerFactory.getLogger(DrugLabelDao.class);

    public boolean existsById(String id) {
        return super.existsById(id, "drug_label");
    }

    public void saveDrugLabel(DrugLabel drugLabel) {
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into drug_label " +
                                "(id, name, obj_cls, alternate_drug_available, dosing_information, prescribing_markdown, source, text_markdown, summary_markdown, efficacy_summary, response_warning, alternative_drug, raw, drug_id) " +
                                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                );

                preparedStatement.setString(1, drugLabel.getId());
                preparedStatement.setString(2, drugLabel.getName());
                preparedStatement.setString(3, drugLabel.getObjCls());
                preparedStatement.setBoolean(4, drugLabel.isAlternateDrugAvailable());
                preparedStatement.setBoolean(5, drugLabel.isDosingInformation());
                preparedStatement.setString(6, drugLabel.getPrescribingMarkdown());
                preparedStatement.setString(7, drugLabel.getSource());
                preparedStatement.setString(8, drugLabel.getTextMarkdown());
                preparedStatement.setString(9, drugLabel.getSummaryMarkdown());
                preparedStatement.setString(10, drugLabel.getEfficacySummary());
                preparedStatement.setString(11, drugLabel.getResponseWarning());
                preparedStatement.setString(12, drugLabel.getAlternativeDrug());
                preparedStatement.setString(13, drugLabel.getRaw());
                preparedStatement.setString(14, drugLabel.getDrugId());

                preparedStatement.execute();

            } catch (SQLException e) {
                log.info("", e);
            }
        });
    }

    public void updateExtendedFields(DrugLabel drugLabel) {
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "update drug_label " +
                                "set efficacy_summary = ?, response_warning = ?, alternative_drug = ? " +
                                "where id = ?"
                );

                preparedStatement.setString(1, drugLabel.getEfficacySummary());
                preparedStatement.setString(2, drugLabel.getResponseWarning());
                preparedStatement.setString(3, drugLabel.getAlternativeDrug());
                preparedStatement.setString(4, drugLabel.getId());

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                log.info("", e);
            }
        });
    }

    public List<DrugLabel> findAll() {
        List<DrugLabel> drugLabels = new ArrayList<>();

        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id, name, obj_cls, alternate_drug_available, dosing_information, prescribing_markdown, source, text_markdown, summary_markdown, efficacy_summary, response_warning, alternative_drug, raw, drug_id " +
                                "from drug_label"
                );

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    DrugLabel drugLabel = buildDrugLabel(resultSet);
                    drugLabels.add(drugLabel);
                }

            } catch (SQLException e) {
                log.info("", e);
            }
        });

        return drugLabels;
    }

    public List<DrugLabel> findByKeyword(String keyword) {
        List<DrugLabel> drugLabels = new ArrayList<>();

        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id, name, obj_cls, alternate_drug_available, dosing_information, prescribing_markdown, source, text_markdown, summary_markdown, efficacy_summary, response_warning, alternative_drug, raw, drug_id " +
                                "from drug_label " +
                                "where id like ? " +
                                "or name like ? " +
                                "or obj_cls like ? " +
                                "or cast(alternate_drug_available as char) like ? " +
                                "or cast(dosing_information as char) like ? " +
                                "or prescribing_markdown like ? " +
                                "or source like ? " +
                                "or text_markdown like ? " +
                                "or summary_markdown like ? " +
                                "or efficacy_summary like ? " +
                                "or response_warning like ? " +
                                "or alternative_drug like ? " +
                                "or raw like ? " +
                                "or drug_id like ?"
                );

                String likeKeyword = "%" + keyword + "%";

                for (int i = 1; i <= 14; i++) {
                    preparedStatement.setString(i, likeKeyword);
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    DrugLabel drugLabel = buildDrugLabel(resultSet);
                    drugLabels.add(drugLabel);
                }

            } catch (SQLException e) {
                log.info("", e);
            }
        });

        return drugLabels;
    }

    private DrugLabel buildDrugLabel(ResultSet resultSet) throws SQLException {
        return new DrugLabel(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("obj_cls"),
                resultSet.getBoolean("alternate_drug_available"),
                resultSet.getBoolean("dosing_information"),
                resultSet.getString("prescribing_markdown"),
                resultSet.getString("source"),
                resultSet.getString("text_markdown"),
                resultSet.getString("summary_markdown"),
                resultSet.getString("efficacy_summary"),
                resultSet.getString("response_warning"),
                resultSet.getString("alternative_drug"),
                resultSet.getString("raw"),
                resultSet.getString("drug_id")
        );
    }
}