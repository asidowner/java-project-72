package hexlet.code.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlFormatter {

    public static String formatURL(String url) throws URISyntaxException {
        var uri = new URI(url).normalize();

        try {
            uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }

        StringBuilder builder = new StringBuilder();

        builder.append(uri.getScheme());
        builder.append("://");
        builder.append(uri.getHost());
        var port = uri.getPort();

        if (port != -1) {
            builder.append(":");
            builder.append(port);
        }

        return builder.toString();
    }
}
