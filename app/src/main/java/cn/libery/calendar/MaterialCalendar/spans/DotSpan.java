package cn.libery.calendar.MaterialCalendar.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import cn.libery.calendar.Constants;
import cn.libery.calendar.utils.PixelUtil;
import cn.libery.calendar.utils.SharedPreferUtil;


/**
 * Span to draw a dot centered under a section of text
 */
public class DotSpan implements LineBackgroundSpan {

    /**
     * Default radius used
     */
    public static final float DEFAULT_RADIUS = 3;

    private final float radius;
    private static int mColor;

    /**
     * @see #DotSpan(float, int)
     * @see #DEFAULT_RADIUS
     */
    public DotSpan() {
        this.radius = DEFAULT_RADIUS;
        mColor = 0;
    }

    /**
     * @see #DotSpan(float, int)
     * @see #DEFAULT_RADIUS
     */
    public DotSpan(int color) {
        this.radius = DEFAULT_RADIUS;
        this.mColor = color;
    }

    /**
     * @see #DotSpan(float, int)
     */
    public DotSpan(float radius) {
        this.radius = radius;
        this.mColor = 0;
    }

    /**
     * Create a span to draw a dot using a specified radius and color
     *
     * @param radius radius for the dot
     * @param color  color of the dot
     */
    public DotSpan(float radius, int color) {
        this.radius = radius;
        this.mColor = color;
    }

    public static void setColor(int color) {
        mColor = color;
    }

    /**
     * Adjust the little red dot position according to the different screen sizes
     */

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        int oldColor = paint.getColor();
        if (mColor != 0) {
            paint.setColor(mColor);
        }
        int with = PixelUtil.getWith();
        if (SharedPreferUtil.get(Constants.IS_LUNAR_CALENDAR, true)) {
            if (with >= 720 && with < 1080) {
                canvas.drawCircle((left + right) / 2, bottom + 28, radius, paint);//little white dot position
            } else if (with >= 1080 && with < 1440) {
                canvas.drawCircle((left + right) / 2, bottom + 48, radius + 2, paint);//little white dot position
            } else {
                canvas.drawCircle((left + right) / 2, bottom + 60, radius + 4, paint);
            }
        } else {
            if (with >= 720 && with < 1080) {
                canvas.drawCircle((left + right) / 2, bottom + radius, radius, paint);
            } else if (with >= 1080 && with < 1440) {
                canvas.drawCircle((left + right) / 2, bottom + radius, radius + 2, paint);
            } else {
                canvas.drawCircle((left + right) / 2, bottom + radius, radius + 4, paint);
            }
        }
        paint.setColor(oldColor);
    }
}
