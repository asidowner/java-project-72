package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.RootController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Settings;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

@Slf4j
public class App {

    private static final Settings SETTINGS = new Settings();

    public static void main(String[] args) throws IOException, SQLException {
        Javalin app = getApp();
        app.start(SETTINGS.getApplicationPort());
    }

    public static Javalin getApp() throws IOException, SQLException {
        App.initDataBase();

        Javalin app = Javalin.create(
                javalinConfig -> {
                    if (SETTINGS.isDebug()) {
                        javalinConfig.plugins.enableDevLogging();
                    }
                }
        );

        app.get(NamedRoutes.rootPath(), RootController::index);

        return app;
    }

    private static void initDataBase() throws IOException, SQLException {
        var sql = readResourceFile("schema.sql");

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(SETTINGS.getDatabaseUrl());

        var dataSource = new HikariDataSource(hikariConfig);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;
    }

    private static String readResourceFile(String fileName) throws IOException {
        var path = Paths.get("src", "main", "resources", fileName);
        return Files.readString(path);
    }
}
