package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.DateTimeFormatter;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private static Javalin app;
    private static MockWebServer server;

    @BeforeAll
    public static void beforeAll() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        server.shutdown();
    }

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

    @Test
    void testUrlCheck() throws SQLException {

        var url = new Url(server.url("").toString(), Timestamp.from(ZonedDateTime.now().toInstant()));
        UrlRepository.save(url);
        String stringMock = "<html lang=\"en\"><head>"
                + "<meta name=\"Description\" content=\"I'm description\">"
                + "<title>Анализатор страниц</title></head>"
                + "<body><h1>I'm header</h1></body>";

        JavalinTest.test(app, (server1, client) -> {
            server.enqueue(new MockResponse().setBody(stringMock).setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(404));

            var response1 = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response1.code()).isEqualTo(200);

            var response2 = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response2.code()).isEqualTo(200);

            var responseUrlDetail = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(responseUrlDetail.code()).isEqualTo(200);
            var responseBody = responseUrlDetail.body().string();
            assertThat(responseBody).contains("200");
            assertThat(responseBody).contains("404");

            var responseUrlList = client.get(NamedRoutes.urlsPath());
            assertThat(responseUrlList.code()).isEqualTo(200);
            var responseBodyList = responseUrlList.body().string();
            assertThat(responseBodyList).contains("404");
            assertThat(responseBodyList).doesNotContain("200");
        });

    }
}
