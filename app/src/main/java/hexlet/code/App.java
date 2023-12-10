package hexlet.code;

import hexlet.code.controller.RootController;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Settings;
import io.javalin.Javalin;

public class App {

    private static final Settings SETTINGS = new Settings();

    public static Javalin getApp() {
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

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(SETTINGS.getApplicationPort());
    }
}
