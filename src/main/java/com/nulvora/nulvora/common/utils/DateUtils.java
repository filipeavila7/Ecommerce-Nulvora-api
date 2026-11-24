package com.nulvora.nulvora.common.utils;




import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null
                ? dateTime.format(DATE_TIME_FORMATTER)
                : null;
    }

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime != null
                ? dateTime.format(DATE_FORMATTER)
                : null;
    }
}
