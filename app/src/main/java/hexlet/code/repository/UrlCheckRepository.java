package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    private static final String FETCH_ALL_TEMPLATE = """
            SELECT
              id,
              url_id,
              status_code,
              h1,
              title,
              description,
              created_at
            FROM url_checks;
            """;
    private static final String FETCH_ALL_BY_URL_ID_TEMPLATE = """
            SELECT
              id,
              url_id,
              status_code,
              h1,
              title,
              description,
              created_at
            FROM url_checks WHERE url_id = ?;
            """;
    private static final String SAVE_ONE_TEMPLATE = """
            INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    public static void save(UrlCheck urlCheck) throws SQLException {
        try (var conn = dataSource.getConnection();
             var prepareStatement = conn.prepareStatement(SAVE_ONE_TEMPLATE, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setLong(1, urlCheck.getUrlId());
            prepareStatement.setInt(2, urlCheck.getStatusCode());
            prepareStatement.setString(3, urlCheck.getH1());
            prepareStatement.setString(4, urlCheck.getTitle());
            prepareStatement.setString(5, urlCheck.getDescription());
            prepareStatement.setTimestamp(6, urlCheck.getCreatedAt());
            prepareStatement.executeUpdate();

            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> filterByUrlId(Long urlId) throws SQLException {
        try (var conn = dataSource.getConnection();
             var prepareStatement = conn.prepareStatement(FETCH_ALL_BY_URL_ID_TEMPLATE)) {
            prepareStatement.setLong(1, urlId);
            prepareStatement.execute();

            var resultSet = prepareStatement.getResultSet();

            return getEntitiesFromResultSet(resultSet);
        }
    }


    public static List<UrlCheck> getEntities() throws SQLException {
        try (var conn = dataSource.getConnection();
             var prepareStatement = conn.prepareStatement(FETCH_ALL_TEMPLATE)) {
            prepareStatement.execute();

            var resultSet = prepareStatement.getResultSet();

            return getEntitiesFromResultSet(resultSet);
        }
    }

    private static List<UrlCheck> getEntitiesFromResultSet(ResultSet resultSet) throws SQLException {
        List<UrlCheck> result = new ArrayList<>();

        while (resultSet.next()) {
            UrlCheck urlCheck = getUrlCheckModelFromResultSet(resultSet);
            result.add(urlCheck);
        }

        return result;
    }

    private static UrlCheck getUrlCheckModelFromResultSet(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var urlId = resultSet.getLong("url_id");
        var statusCode = resultSet.getInt("status_code");
        var h1 = resultSet.getString("h1");
        var title = resultSet.getString("title");
        var description = resultSet.getString("description");
        var createdAt = resultSet.getTimestamp("created_at");

        UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
        urlCheck.setId(id);
        return urlCheck;
    }
}
