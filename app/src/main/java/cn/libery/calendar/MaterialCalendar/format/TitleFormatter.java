package cn.libery.calendar.MaterialCalendar.format;


import cn.libery.calendar.MaterialCalendar.CalendarDay;

/**
*/
public interface TitleFormatter {

    /**
     * Converts the supplied day to a suitable month/year title
     *
     * @param day the day containing relevant month and year information
     *
     * @return a label to display for the given month/year
     */
    CharSequence format(CalendarDay day);
}
