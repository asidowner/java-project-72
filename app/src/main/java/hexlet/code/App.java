package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.RootController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Settings;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.stream.Collectors;

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
        var sql = getSqlSchema();

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(SETTINGS.getDatabaseUrl());

        try (var dataSource = new HikariDataSource(hikariConfig);
             var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
            BaseRepository.dataSource = dataSource;
        }
    }

    private static String getSqlSchema() throws IOException {
        var url = App.class.getClassLoader().getResource("schema.sql");
        File file;
        try {
            file = new File(url.getFile());
        } catch (NullPointerException e) {
            log.info("Schema file not found;");
            log.debug(e.toString());
            throw e;
        }

        try (var lines = Files.lines(file.toPath())) {
            return lines.collect(Collectors.joining("\n"));
        }
    }
}
