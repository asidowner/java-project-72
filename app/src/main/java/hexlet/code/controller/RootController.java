package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.util.FlashEnum;
import io.javalin.http.Context;

import java.util.Collections;

public class RootController {
    public static void index(Context ctx) {
        var flash = (String) ctx.consumeSessionAttribute("flash");
        var flashType = (String) ctx.consumeSessionAttribute("flashType");

        var page = new BasePage();

        if (flash != null && flashType != null) {
            page.setFlash(flash);
            page.setFlashType(FlashEnum.valueOf(flashType));
        }

        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
