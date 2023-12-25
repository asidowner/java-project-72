package hexlet.code.util;

import hexlet.code.dto.BasePage;
import io.javalin.http.Context;

public class FlashWorker {

    public static void handler(Context ctx, BasePage page) {
        var flash = (String) ctx.consumeSessionAttribute("flash");
        var flashType = (String) ctx.consumeSessionAttribute("flashType");

        if (flash != null && flashType != null) {
            page.setFlash(flash);
            page.setFlashType(FlashEnum.valueOf(flashType));
        }
    }

    public static void create(Context ctx, String flashMessage, FlashEnum flashType) {
        ctx.sessionAttribute("flash", flashMessage);
        ctx.sessionAttribute("flashType", flashType.toString());
    }
}
