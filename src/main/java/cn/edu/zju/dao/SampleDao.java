package cn.edu.zju.dao;

import cn.edu.zju.bean.Sample;
import cn.edu.zju.dbutils.DBUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SampleDao extends BaseDao {

    public int save(String uploadedBy) {
        AtomicInteger key = new AtomicInteger();

        DBUtils.execSQL(connection -> {
            String sql = "insert into sample(created_at, uploaded_by) values (?, ?)";

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
                preparedStatement.setString(2, uploadedBy);
                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        key.set(generatedKeys.getInt(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save sample.", e);
            }
        });

        return key.get();
    }

    public List<Sample> findAll() {
        List<Sample> samples = new ArrayList<>();

        DBUtils.execSQL(connection -> {
            String sql = "select id, created_at, uploaded_by from sample order by created_at desc, id desc";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    samples.add(readSample(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to find samples.", e);
            }
        });

        return samples;
    }

    public List<Sample> findByUploadedBy(String uploadedBy) {
        List<Sample> samples = new ArrayList<>();

        DBUtils.execSQL(connection -> {
            String sql = "select id, created_at, uploaded_by from sample " +
                    "where uploaded_by = ? " +
                    "order by created_at desc, id desc";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, uploadedBy);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        samples.add(readSample(resultSet));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to find samples by user.", e);
            }
        });

        return samples;
    }

    public Sample findById(int id) {
        AtomicReference<Sample> sample = new AtomicReference<>();

        DBUtils.execSQL(connection -> {
            String sql = "select id, created_at, uploaded_by from sample where id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        sample.set(readSample(resultSet));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to find sample by id.", e);
            }
        });

        return sample.get();
    }

    private Sample readSample(ResultSet resultSet) throws SQLException {
        int sampleId = resultSet.getInt("id");
        Timestamp timestamp = resultSet.getTimestamp("created_at");
        Date createdAt = timestamp == null ? null : new Date(timestamp.getTime());
        String uploadedBy = resultSet.getString("uploaded_by");

        return new Sample(sampleId, createdAt, uploadedBy);
    }
}