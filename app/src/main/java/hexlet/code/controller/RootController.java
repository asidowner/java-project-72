package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.util.FlashWorker;
import io.javalin.http.Context;

import java.util.Collections;

public class RootController {
    public static void index(Context ctx) {
        var page = new BasePage();

        FlashWorker.handler(ctx, page);

        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
