package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.DateTimeFormatter;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("Анализатор страниц</h1>");
        }));
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");


            requestBody = "url=http://example.com:8080";
            response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com:8080");

            requestBody = "url=http://example.com:8089/abc";
            response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com:8089");
        });
    }

    @Test
    public void testCreateBadUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=exampleBadUrl.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);

            response = client.get(NamedRoutes.urlsPath());
            assertThat(response.body().string()).doesNotContain("exampleBadUrl.com");


            response = client.post(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(400);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url("https://example.com", Timestamp.from(ZonedDateTime.now().toInstant()));
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            var responseBody = response.body().string();
            assertThat(responseBody).contains(DateTimeFormatter.format(url.getCreatedAt()));
            assertThat(responseBody).contains(url.getName());
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (((server, client) -> {
            var response = client.get(NamedRoutes.urlPath(99999L));
            assertThat(response.code()).isEqualTo(404);
        })));
    }
}
