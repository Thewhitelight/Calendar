package cn.libery.calendar.MaterialCalendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;

import java.util.List;

import cn.libery.calendar.Constants;
import cn.libery.calendar.MaterialCalendar.format.DayFormatter;
import cn.libery.calendar.MaterialCalendar.spans.DotSpan;
import cn.libery.calendar.R;
import cn.libery.calendar.utils.PixelUtil;
import cn.libery.calendar.utils.SharedPreferUtil;

import static cn.libery.calendar.MaterialCalendar.MaterialCalendarView.SHOW_DEFAULTS;
import static cn.libery.calendar.MaterialCalendar.MaterialCalendarView.showDecoratedDisabled;
import static cn.libery.calendar.MaterialCalendar.MaterialCalendarView.showOtherMonths;
import static cn.libery.calendar.MaterialCalendar.MaterialCalendarView.showOutOfRange;

/**
 * Display one day of a {@linkplain MaterialCalendarView}
 */
@SuppressLint("ViewConstructor")
class DayView extends CheckedTextView {

    private CalendarDay date;
    private int selectionColor = Color.BLACK;

    private Drawable customBackground = null;
    private Drawable selectionDrawable;
    private DayFormatter formatter = DayFormatter.DEFAULT;

    private boolean isInRange = true;
    private int showOtherDates = SHOW_DEFAULTS;
    private boolean isDecoratedDisabled = false;
    private LunarCalendar lunarCalendar;
    private String lunarDate;
    private boolean isShowLunar = SharedPreferUtil.get(Constants.IS_LUNAR_CALENDAR, true);
    private boolean isHoliday = SharedPreferUtil.get(Constants.IS_HOLIDAY_CALENDAR, true);

    public DayView(Context context, CalendarDay day) {
        super(context);

        //fadeTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        setSelectionColor(this.selectionColor);

        setGravity(Gravity.CENTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }
        lunarCalendar = new LunarCalendar();
        setDay(day);

    }

    public void setDay(CalendarDay date) {
        this.date = date;
        lunarCalendar.setHoliday(isHoliday);
        if (isShowLunar) {
            lunarDate = "\n" + lunarCalendar.getLunarDate(date.getYear(), (date.getMonth() + 1), date.getDay(), false);
        } else {
            lunarDate = "";
        }
        setText(getLabel());
    }

    /**
     * Set the new label formatter and reformat the current label. This preserves current spans.
     *
     * @param formatter new label formatter
     */
    public void setDayFormatter(DayFormatter formatter) {
        this.formatter = formatter == null ? DayFormatter.DEFAULT : formatter;
        CharSequence currentLabel = getText();
        Object[] spans = null;
        if (currentLabel instanceof Spanned) {
            spans = ((Spanned) currentLabel).getSpans(0, currentLabel.length(), Object.class);
        }
        SpannableString newLabel = new SpannableString(getLabel());
        if (spans != null) {
            for (Object span : spans) {
                newLabel.setSpan(span, 0, newLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(newLabel);
    }


    @NonNull
    public String getLabel() {
        String s = formatter.format(date);
        StringBuilder sb = new StringBuilder(s);
        if (isShowLunar) {
            sb.append("\n");
        }
        return sb.toString();
    }

    public void setSelectionColor(int color) {
        this.selectionColor = color;
        regenerateBackground();
    }

    /**
     * @param selectionDrawable custom selection drawable
     */
    public void setSelectionDrawable(Drawable selectionDrawable) {
        this.selectionDrawable = selectionDrawable;
        invalidate();
    }

    /**
     * @param customBackground background to draw behind everything else
     */
    public void setCustomBackground(Drawable customBackground) {
        this.customBackground = customBackground;
        invalidate();
    }

    public CalendarDay getDate() {
        return date;
    }

    private void setEnabled() {
        boolean isInMonth = true;
        boolean enabled = isInMonth && isInRange && !isDecoratedDisabled;
        super.setEnabled(enabled);

        boolean showOtherMonths = showOtherMonths(showOtherDates);
        boolean showOutOfRange = showOutOfRange(showOtherDates) || showOtherMonths;
        boolean showDecoratedDisabled = showDecoratedDisabled(showOtherDates);

        boolean shouldBeVisible = enabled;

        if (!isInMonth && showOtherMonths) {
            shouldBeVisible = true;
        }

        if (!isInRange && showOutOfRange) {
            shouldBeVisible |= isInMonth;
        }

        if (isDecoratedDisabled && showDecoratedDisabled) {
            shouldBeVisible |= isInMonth && isInRange;
        }

        setVisibility(shouldBeVisible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setupSelection(@MaterialCalendarView.ShowOtherDates int showOtherDates, boolean inRange, boolean inMonth) {
        this.showOtherDates = showOtherDates;
        this.isInRange = inMonth && inRange;
        setEnabled();
    }


    private final Rect tempRect = new Rect();
    private final Paint paint_string = new Paint();
    boolean flag = false;
    private int main_black = getContext().getResources().getColor(R.color.main_black);
    private int calendar_gray = getContext().getResources().getColor(R.color.calendar_gray);

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (customBackground != null) {
            canvas.getClipBounds(tempRect);
            customBackground.setBounds(tempRect);
            customBackground.setState(getDrawableState());
            customBackground.draw(canvas);
        }
        paint_string.setSubpixelText(true);
        paint_string.setAntiAlias(true);
        if (isChecked()) {
            setTextColor(main_black);
            paint_string.setColor(main_black);
            DotSpan.setColor(getContext().getResources().getColor(R.color.calendar_bg));
        } else {
            if (isEnabled()) {
                if (flag) {
                    setColor(calendar_gray);
                } else {
                    setColor(Color.WHITE);
                }
            } else {
                setEnabled(true); //非当月可点击
                flag = true;
                setColor(calendar_gray);
            }
        }
        paint_string.setTextSize(PixelUtil.dp2px(9));
        if (PixelUtil.getWith() >= 720 && PixelUtil.getWith() < 1080) {
            if (isThreeText()) {
                canvas.drawText(lunarDate, getGravity() - 4, getGravity() + 46, paint_string);
            } else {
                canvas.drawText(lunarDate, getGravity() + 7, getGravity() + 46, paint_string);
            }
        } else if (PixelUtil.getWith() >= 1080 && PixelUtil.getWith() < 1440) {
            if (isThreeText()) {
                canvas.drawText(lunarDate, getGravity() + 7, getGravity() + 83, paint_string);
            } else {
                canvas.drawText(lunarDate, getGravity() + 21, getGravity() + 83, paint_string);
            }
        } else if (PixelUtil.getWith() >= 1440) {
            if (isThreeText()) {
                canvas.drawText(lunarDate, getGravity() + 18, getGravity() + 115, paint_string);
            } else {
                canvas.drawText(lunarDate, getGravity() + 33, getGravity() + 115, paint_string);
            }
        }

        super.onDraw(canvas);
    }

    private boolean isThreeText() {
        return lunarDate.length() == 4;
    }

    private void setColor(@ColorInt int color) {
        setTextColor(color);
        paint_string.setColor(color);
        DotSpan.setColor(color);
    }

    private void regenerateBackground() {
        if (selectionDrawable != null) {
            setBackgroundDrawable(selectionDrawable);
        } else {
            int fadeTime = 100;
            setBackgroundDrawable(generateBackground(selectionColor, fadeTime));
        }
    }

    private static Drawable generateBackground(int color, int fadeTime) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.setExitFadeDuration(fadeTime);
        drawable.addState(new int[]{android.R.attr.state_checked}, generateRectangleDrawable(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.addState(new int[]{android.R.attr.state_pressed}, generateRippleDrawable(color));
        } else {
            drawable.addState(new int[]{android.R.attr.state_checked}, generateRectangleDrawable(color));
        }
        drawable.addState(new int[]{}, generateRectangleDrawable(Color.TRANSPARENT));

        return drawable;
    }

    private static Drawable generateRectangleDrawable(final int color) {
        ShapeDrawable drawable = new ShapeDrawable(new RectShape());
        drawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(0, 0, 0, 0, color, color, Shader.TileMode.REPEAT);
            }
        });
        return drawable;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable generateRippleDrawable(final int color) {
        ColorStateList list = ColorStateList.valueOf(color);
        Drawable mask = generateRectangleDrawable(Color.WHITE);
        return new RippleDrawable(list, null, mask);
    }

    /**
     * @param facade apply the facade to us
     */
    void applyFacade(DayViewFacade facade) {
        this.isDecoratedDisabled = facade.areDaysDisabled();
        setEnabled();

        setCustomBackground(facade.getBackgroundDrawable());
        setSelectionDrawable(facade.getSelectionDrawable());

        // Facade has spans
        List<DayViewFacade.Span> spans = facade.getSpans();
        if (!spans.isEmpty()) {
            String label = getLabel();
            SpannableString formattedLabel = new SpannableString(getLabel());
            for (DayViewFacade.Span span : spans) {
                formattedLabel.setSpan(span.span, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(formattedLabel);
        }
        // Reset in case it was customized previously
        else {
            setText(getLabel());
        }
    }
}
