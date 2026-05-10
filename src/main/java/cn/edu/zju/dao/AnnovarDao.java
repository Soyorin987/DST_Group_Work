package cn.edu.zju.dao;

import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnovarDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(AnnovarDao.class);

    public void save(int sampleId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("TSV file is empty.");
        }

        content = content.replaceFirst("^\\uFEFF", "");
        String[] lines = content.split("\\R");

        if (lines.length < 2) {
            throw new IllegalArgumentException("TSV file must contain a header row and at least one data row.");
        }

        String[] headers = lines[0].split("\\t", -1);
        Map<String, Integer> headerIndex = buildHeaderIndex(headers);

        validateRequiredColumns(headerIndex);

        DBUtils.execSQL(connection -> {
            String sql = "INSERT INTO annovar " +
                    "(sample_id, Chr, Start, End, Ref, Alt, `Func.refGene`, `Gene.refGene`, " +
                    "`GeneDetail.refGene`, `ExonicFunc.refGene`, `AAChange.refGene`, Otherinfo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try {
                connection.setAutoCommit(false);

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    int batchCount = 0;

                    for (int i = 1; i < lines.length; i++) {
                        String line = lines[i];

                        if (line == null || line.trim().isEmpty()) {
                            continue;
                        }

                        String[] row = line.split("\\t", -1);

                        validateRequiredValues(row, headerIndex, i + 1);

                        preparedStatement.setInt(1, sampleId);
                        preparedStatement.setString(2, getValue(row, headerIndex, "Chr"));
                        preparedStatement.setString(3, getValue(row, headerIndex, "Start"));
                        preparedStatement.setString(4, getValue(row, headerIndex, "End"));
                        preparedStatement.setString(5, getValue(row, headerIndex, "Ref"));
                        preparedStatement.setString(6, getValue(row, headerIndex, "Alt"));
                        preparedStatement.setString(7, getValue(row, headerIndex, "Func.refGene"));
                        preparedStatement.setString(8, getValue(row, headerIndex, "Gene.refGene"));
                        preparedStatement.setString(9, getValue(row, headerIndex, "GeneDetail.refGene"));
                        preparedStatement.setString(10, getValue(row, headerIndex, "ExonicFunc.refGene"));
                        preparedStatement.setString(11, getValue(row, headerIndex, "AAChange.refGene"));
                        preparedStatement.setString(12, line);

                        preparedStatement.addBatch();
                        batchCount++;

                        if (batchCount % 1000 == 0) {
                            preparedStatement.executeBatch();
                            connection.commit();
                        }
                    }

                    preparedStatement.executeBatch();
                    connection.commit();
                }
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    log.error("Rollback failed", rollbackException);
                }

                throw new RuntimeException("Failed to save TSV mutation data.", e);
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    log.error("Failed to reset auto commit", e);
                }
            }
        });
    }

    private Map<String, Integer> buildHeaderIndex(String[] headers) {
        Map<String, Integer> headerIndex = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            String header = headers[i] == null ? "" : headers[i].trim();
            headerIndex.put(header, i);
        }

        return headerIndex;
    }

    private void validateRequiredColumns(Map<String, Integer> headerIndex) {
        String[] requiredColumns = {
                "Chr",
                "Start",
                "End",
                "Ref",
                "Alt",
                "Gene.refGene"
        };

        for (String column : requiredColumns) {
            if (!headerIndex.containsKey(column)) {
                throw new IllegalArgumentException("Missing required column: " + column);
            }
        }
    }

    private void validateRequiredValues(String[] row, Map<String, Integer> headerIndex, int rowNumber) {
        String[] requiredColumns = {
                "Chr",
                "Start",
                "End",
                "Ref",
                "Alt",
                "Gene.refGene"
        };

        for (String column : requiredColumns) {
            String value = getValue(row, headerIndex, column);

            if (value == null || value.isBlank() || ".".equals(value)) {
                throw new IllegalArgumentException("Missing required value at row " + rowNumber + ": " + column);
            }
        }
    }

    private String getValue(String[] row, Map<String, Integer> headerIndex, String columnName) {
        Integer index = headerIndex.get(columnName);

        if (index == null || index >= row.length) {
            return null;
        }

        String value = row[index];

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.isEmpty()) {
            return null;
        }

        return value;
    }

    public List<String> getRefGenes(int sampleId) {
        String sql = "select distinct `Gene.refGene` from annovar " +
                "where sample_id = ? " +
                "and `Gene.refGene` is not null " +
                "and `Gene.refGene` != '' " +
                "and `Gene.refGene` != '.' " +
                "and (`ExonicFunc.refGene` is null or `ExonicFunc.refGene` != 'synonymous SNV')";

        Set<String> genes = new LinkedHashSet<>();

        DBUtils.execSQL(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, sampleId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String geneValue = resultSet.getString(1);

                        if (geneValue == null || geneValue.isBlank()) {
                            continue;
                        }

                        String[] splitGenes = geneValue.split("[,;]");

                        for (String gene : splitGenes) {
                            String cleanGene = gene.trim();

                            if (!cleanGene.isEmpty() && !".".equals(cleanGene)) {
                                genes.add(cleanGene);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to read reference genes.", e);
            }
        });

        return new ArrayList<>(genes);
    }
}