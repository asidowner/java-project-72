package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.FlashEnum;
import hexlet.code.util.FlashWorker;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Slf4j
public class UrlCheckController {
    public static void create(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class)
                .get();

        var url = UrlRepository.find(id).orElseThrow(NotFoundResponse::new);

        try {
            var response = Unirest.get(url.getName()).asString();

            var status = response.getStatus();

            var h1 = "";
            var title = "";
            var description = "";
            var ts = Timestamp.from(ZonedDateTime.now().toInstant());

            var urlCheck = new UrlCheck(status, title, h1, description, url.getId(), ts);

            try {
                UrlCheckRepository.save(urlCheck);
            } catch (SQLException e) {
                log.error("Exception: ", e);
                throw new InternalServerErrorResponse();
            }

            FlashWorker.create(ctx, "Страница успешно проверена", FlashEnum.success);
        } catch (UnirestException e) {
            log.error("Request exception: ", e);
            FlashWorker.create(ctx, "Некорректный адрес", FlashEnum.danger);
        }

        ctx.redirect(NamedRoutes.urlPath(url.getId()));
    }
}
