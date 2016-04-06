package cn.libery.calendar.MaterialCalendar;


import android.content.Context;

import java.util.Collection;
import java.util.HashSet;

import cn.libery.calendar.MaterialCalendar.spans.DotSpan;


/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view, Context context) {
        view.addSpan(new DotSpan(4, color));
    }
}
