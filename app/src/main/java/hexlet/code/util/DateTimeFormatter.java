package hexlet.code.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateTimeFormatter {
    private static final String PATTERN = "MM/dd/yyyy' 'HH:mm";

    public static String format(Timestamp ts) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
        return simpleDateFormat.format(ts);
    }
}
