package hexlet.code;

import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {

    private static Javalin app;

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    void testMainPage() {

        JavalinTest.test(app, ((server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertEquals(response.code(), 200);
            assertNotNull(response.body());
            assertEquals(response.body().string(), "Hello World");
        }));
    }
}
