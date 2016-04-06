package cn.libery.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import cn.libery.calendar.MaterialCalendar.CalendarDay;
import cn.libery.calendar.MaterialCalendar.MaterialCalendarView;
import cn.libery.calendar.MaterialCalendar.OnDateLongChangedListener;
import cn.libery.calendar.MaterialCalendar.OnDateSelectedListener;
import cn.libery.calendar.MaterialCalendar.OnMonthChangedListener;
import cn.libery.calendar.MaterialCalendar.OneDayDecorator;
import cn.libery.calendar.widget.WeekDayView;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener,
        OnMonthChangedListener, OnDateLongChangedListener {

    private MaterialCalendarView mCalendarView;
    private CalendarDay mCurrentDay, monthDate;
    private boolean backCurrentDay;
    private OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrentDay = CalendarDay.today();
        monthDate = mCurrentDay;
        mCalendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        mCalendarView.setMaximumDate(CalendarDay.from(2048, 11, 31));
        mCalendarView.setMinimumDate(CalendarDay.from(1970, 0, 1));
        mCalendarView.setOnDateChangedListener(this);
        mCalendarView.setOnMonthChangedListener(this);
        mCalendarView.setOnDateLongChangedListener(this);
        mCalendarView.setSelectedDate(mCurrentDay);
        mCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        mCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        mCalendarView.setSelectionColor(Color.WHITE);
        mCalendarView.addDecorators(oneDayDecorator);
        mCalendarView.setDateTextAppearance(R.style.TextAppearance_MaterialCalendarWidget_Date);
        Button backToday = (Button) findViewById(R.id.back_today);
        if (backToday != null) {
            backToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mCalendarView.setCurrentDate(mCurrentDay, false);
                    mCalendarView.setSelectedDate(mCurrentDay);
                }
            });
        }
        final WeekDayView weekDayView = (WeekDayView) findViewById(R.id.week);
        if (weekDayView != null) {
            weekDayView.setWeekDayStart(Calendar.SUNDAY);
            mCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        }
        Button change = (Button) findViewById(R.id.change);
        if (change != null && weekDayView != null) {
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mCalendarView.setInvalidate();
                    weekDayView.setWeekDayStart(Calendar.MONDAY);
                    mCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
                    mCalendarView.setSelectedDate(mCurrentDay);

                    mCalendarView.removeDecorators();
                    oneDayDecorator.setDate(mCurrentDay.getDate());
                    mCalendarView.addDecorator(oneDayDecorator);
                }
            });
        }
    }

    @Override
    public void onDateLongChanged(@NonNull final MaterialCalendarView widget, @Nullable final CalendarDay date) {
        mCalendarView.clearSelection();
        mCalendarView.setSelectedDate(date);
        Toast.makeText(this, date.getDate().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDateSelected(@NonNull final MaterialCalendarView widget, @NonNull final CalendarDay date, final
    boolean selected) {
        oneDayDecorator.setDate(mCurrentDay.getDate());
        if (date.getMonth() != monthDate.getMonth()) {
            backCurrentDay = true;
            mCalendarView.setCurrentDate(date, false);
            mCalendarView.setSelectedDate(date);
        } else {
            backCurrentDay = false;
        }
        Toast.makeText(this, date.getDate().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChanged(final MaterialCalendarView widget, final CalendarDay day) {
        if (backCurrentDay) {
            backCurrentDay = false;
            mCalendarView.setSelectedDate(mCurrentDay);
        } else {
            if (CalendarDay.today().getYear() == day.getYear() && CalendarDay.today().getMonth()
                    == day.getMonth()) {
                mCalendarView.setSelectedDate(CalendarDay.today());
            } else {
                mCalendarView.setSelectedDate(day);
            }
        }
    }
}
