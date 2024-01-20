package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

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

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }

    private static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
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
            assertThat(UrlRepository.exists("https://example.com")).isTrue();

            requestBody = "url=http://example.com:8080";
            response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com:8080");
            assertThat(UrlRepository.exists("http://example.com:8080")).isTrue();

            requestBody = "url=http://example.com:8089/abc";
            response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com:8089");
            assertThat(UrlRepository.exists("http://example.com:8089")).isTrue();
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
            assertThat(UrlRepository.exists("exampleBadUrl.com")).isFalse();


            response = client.post(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(400);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url("https://example.com");
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
    void testUrlCheck() throws IOException, SQLException {
        var url = new Url(server.url("").toString());
        UrlRepository.save(url);

        MockResponse mockedResponse = new MockResponse()
                .setBody(readFixture("urlCheck1.html")).setResponseCode(200);
        server.enqueue(mockedResponse);

        JavalinTest.test(app, (server1, client) -> {
            var response = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);

            var responseUrlDetail = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(responseUrlDetail.code()).isEqualTo(200);

            var title = "Анализатор страниц";
            var firstHeader = "I'm header";
            var secondHeader = "I'm second header";
            var description = "I'm description";

            var responseBody = responseUrlDetail.body().string();
            assertThat(responseBody)
                    .contains("200")
                    .contains(title)
                    .contains(firstHeader)
                    .doesNotContain(secondHeader)
                    .contains(description);

            var urlChecks = UrlCheckRepository.filterByUrlId(url.getId());
            assertThat(urlChecks).isNotEmpty();
            assertThat(urlChecks.size()).isEqualTo(1);

            var urlCheck = urlChecks.get(0);
            assertThat(urlCheck.getTitle()).isEqualTo(title);
            assertThat(urlCheck.getH1()).isEqualTo(firstHeader);
            assertThat(urlCheck.getDescription()).isEqualTo(description);
        });
    }

    @Test
    void testUrlCheckWithoutDescriptionAndHeader() throws IOException, SQLException {
        var url = new Url(server.url("").toString());
        UrlRepository.save(url);

        MockResponse mockedResponse = new MockResponse()
                .setBody(readFixture("urlCheck2.html")).setResponseCode(200);
        server.enqueue(mockedResponse);

        JavalinTest.test(app, (server1, client) -> {
            var response = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);

            var responseUrlDetail = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(responseUrlDetail.code()).isEqualTo(200);


            var title = "Title 2";
            var otherHeader = "Other header";

            var responseBody = responseUrlDetail.body().string();
            assertThat(responseBody)
                    .contains("200")
                    .contains(title)
                    .doesNotContain(otherHeader);

            var urlChecks = UrlCheckRepository.filterByUrlId(url.getId());
            assertThat(urlChecks).isNotEmpty();
            assertThat(urlChecks.size()).isEqualTo(1);
            var urlCheck = urlChecks.get(0);
            assertThat(urlCheck.getTitle()).isEqualTo(title);
        });
    }

    @Test
    void testUrlCheckLastCheck() throws SQLException {
        var url = new Url(server.url("").toString());
        UrlRepository.save(url);

        MockResponse mockedResponse1 = new MockResponse().setResponseCode(200);
        MockResponse mockedResponse2 = new MockResponse().setResponseCode(404);

        server.enqueue(mockedResponse1);
        server.enqueue(mockedResponse2);

        JavalinTest.test(app, (server1, client) -> {
            var response1 = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response1.code()).isEqualTo(200);
            var response2 = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response2.code()).isEqualTo(200);

            var responseUrlDetail = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(responseUrlDetail.code()).isEqualTo(200);

            var responseBody = responseUrlDetail.body().string();
            assertThat(responseBody)
                    .contains("200")
                    .contains("404");

            var responseUrlList = client.get(NamedRoutes.urlsPath());
            assertThat(responseUrlList.code()).isEqualTo(200);
            var responseBodyList = responseUrlList.body().string();

            assertThat(responseBodyList)
                    .contains("404")
                    .doesNotContain("200");
        });
    }
}
