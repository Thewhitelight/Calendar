package cn.libery.calendar.MaterialCalendar;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.Date;

import cn.libery.calendar.R;

/**
 * Decorate a day by custom image
 */
public class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;

    public OneDayDecorator() {
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view, Context context) {
        //view.addSpan(new StyleSpan(Typeface.BOLD));
        //view.addSpan(new RelativeSizeSpan(1.4f));
        Drawable drawable = context.getResources().getDrawable(R.drawable.bg_calendar_decorator);
        if (drawable != null) {
            view.setBackgroundDrawable(drawable);//当天
        }
    }

    /**
     * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
     */
    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
