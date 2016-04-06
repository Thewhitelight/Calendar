package cn.libery.calendar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import cn.libery.calendar.R;

/**
 * Created by Libery on 2015/11/10.
 * Email:libery.szq@qq.com
 */
public class WeekDayView extends LinearLayout {

    private TextView one, two, three, four, five, six, seven;

    public WeekDayView(Context context) {
        super(context);
        init(context);
    }

    public WeekDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_week_view, this);
        one = (TextView) findViewById(R.id.week_one);
        two = (TextView) findViewById(R.id.week_two);
        three = (TextView) findViewById(R.id.week_three);
        four = (TextView) findViewById(R.id.week_four);
        five = (TextView) findViewById(R.id.week_five);
        six = (TextView) findViewById(R.id.week_six);
        seven = (TextView) findViewById(R.id.week_seven);
    }

    public void setWeekDayStart(int day) {
        switch (day) {
            case Calendar.MONDAY:
                one.setText("一");
                two.setText("二");
                three.setText("三");
                four.setText("四");
                five.setText("五");
                six.setText("六");
                seven.setText("日");
                break;
            case Calendar.SUNDAY:
                one.setText("日");
                two.setText("一");
                three.setText("二");
                four.setText("三");
                five.setText("四");
                six.setText("五");
                seven.setText("六");
                break;
        }
    }
}
