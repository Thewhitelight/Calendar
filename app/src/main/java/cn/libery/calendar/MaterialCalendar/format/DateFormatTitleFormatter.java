package cn.libery.calendar.MaterialCalendar.format;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.libery.calendar.MaterialCalendar.CalendarDay;

/**
 * Format using a {@linkplain DateFormat} instance.
 */
public class DateFormatTitleFormatter implements TitleFormatter {

    private final DateFormat dateFormat;

    /**
     * Format using "MMMM yyyy" for formatting
     */
    public DateFormatTitleFormatter() {
        this.dateFormat = new SimpleDateFormat(
                "MMMM yyyy", Locale.getDefault()
        );
    }

    /**
     * Format using a specified {@linkplain DateFormat}
     *
     * @param format the format to use
     */
    public DateFormatTitleFormatter(DateFormat format) {
        this.dateFormat = format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence format(CalendarDay day) {
        return dateFormat.format(day.getDate());
    }
}
