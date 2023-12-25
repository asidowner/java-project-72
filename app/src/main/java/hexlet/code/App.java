package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlCheckController;
import hexlet.code.controller.UrlController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Settings;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        JavalinJte.init(createTemplateEngine());

        Javalin app = Javalin.create(
                javalinConfig -> {
                    if (SETTINGS.isDebug()) {
                        javalinConfig.plugins.enableDevLogging();
                    }
                    javalinConfig.staticFiles.add(staticFileConfig -> {
                        staticFileConfig.hostedPath = NamedRoutes.staticPath();
                        staticFileConfig.directory = "static";
                    });
                }
        );

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);
        app.post(NamedRoutes.urlChecksPath("{id}"), UrlCheckController::create);

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
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }

    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
