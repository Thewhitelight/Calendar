package cn.libery.calendar.MaterialCalendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Libery on 2015/9/10.
 * Email:libery.szq@qq.com
 */
public interface OnDateLongChangedListener {
    void onDateLongChanged(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date);
}
