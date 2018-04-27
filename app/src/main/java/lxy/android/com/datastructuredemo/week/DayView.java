package lxy.android.com.datastructuredemo.week;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MutableBoolean;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import android.widget.ViewSwitcher;

import java.util.Calendar;
import java.util.TimeZone;

import lxy.android.com.datastructuredemo.R;

/**
 * Created by LIXIAOYE on 2018/4/26.
 */

public class DayView extends View {
    private static final String TAG = "DayView";

    private Context mContext;

    private int mViewHeight;
    private int mViewWidth;

    private Time mCurrentTime;
    Time mBaseDate;
    private int mTodayJulianDay;
    private int mNumDays;
    private final Rect mRect = new Rect();
    private final Rect mDestRect = new Rect();
    private boolean mStartingScroll = false;
    /**
     * The selection modes are HIDDEN, PRESSED, SELECTED, and LONGPRESS.
     */
    private static final int SELECTION_HIDDEN = 0;
    private static final int SELECTION_PRESSED = 1; // D-pad down but not up yet
    private static final int SELECTION_SELECTED = 2;
    private static final int SELECTION_LONGPRESS = 3;

    private int mSelectionMode = SELECTION_HIDDEN;

    private boolean mScrolling = false;

    // Pixels scrolled
    private float mInitialScrollX;
    private float mInitialScrollY;

    public DayView(Context context) {
        this(context, null);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ViewSwitcher mViewSwitcher;
    private GestureDetector mGestureDetector;
    private EdgeEffect mEdgeEffectTop;
    private EdgeEffect mEdgeEffectBottom;

    public DayView(Context activity, ViewSwitcher viewSwitcher, int numDays) {
        super(activity);
        mContext = activity;
        mNumDays = numDays;
        mViewSwitcher = viewSwitcher;
        mGestureDetector = new GestureDetector(activity, new CalendarGestureListener());
        mScroller = new OverScroller(activity);
        mEdgeEffectTop = new EdgeEffect(activity);
        mEdgeEffectBottom = new EdgeEffect(activity);
        init(activity);
    }

    private String[] mDayStrs;
    private String[] mDayStrs2Letter;
    private boolean mIs24HourFormat;
    private static float DATE_HEADER_FONT_SIZE = 32;
    private static float DAY_HEADER_FONT_SIZE = 14;
    protected Resources mResources;
    private int mDateStrWidth;

    private void init(Context activity) {
        String timezone = TimeZone.getDefault().getID();
        mCurrentTime = new Time(timezone);
        long currentTimeMillis = System.currentTimeMillis();
        mCurrentTime.set(currentTimeMillis);
        mBaseDate.timezone = timezone;
        mBaseDate.normalize(true);
        mCurrentTime.switchTimezone(timezone);
        mTodayJulianDay = Time.getJulianDay(currentTimeMillis, mCurrentTime.gmtoff);
        Log.e(TAG, "DayView构造函数");
        mResources = activity.getResources();
        DATE_HEADER_FONT_SIZE = (int) mResources.getDimension(R.dimen.date_header_text_size);
        DAY_HEADER_FONT_SIZE = (int) mResources.getDimension(R.dimen.day_label_text_size);
        recalc();
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            int index = i - Calendar.SUNDAY;
            mDayStrs[index] = DateUtils.getDayOfWeekString(i, DateUtils.LENGTH_MEDIUM).toUpperCase();
            mDayStrs[index + 7] = mDayStrs[index];
            mDayStrs2Letter[index] = DateUtils.getDayOfWeekString(i, DateUtils.LENGTH_SHORT).toUpperCase();

            // If we don't have 2-letter day strings, fall back to 1-letter.
            if (mDayStrs2Letter[index].equals(mDayStrs[index])) {
                mDayStrs2Letter[index] = DateUtils.getDayOfWeekString(i, DateUtils.LENGTH_SHORTEST);
            }
            mDayStrs2Letter[index + 7] = mDayStrs2Letter[index];
        }

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(false);
        p.setTextSize(DATE_HEADER_FONT_SIZE);
        String[] dateStrs = {"28", "30"};
        mDateStrWidth = computeMaxStringWidth(0, dateStrs, p);
        p.setTextSize(DAY_HEADER_FONT_SIZE);
        mDateStrWidth += computeMaxStringWidth(0, mDayStrs, p);

    }
    private int mMonthLength;
    private int mFirstVisibleDate;
    private int mFirstVisibleDayOfWeek;
    private void recalc() {
        final long start = mBaseDate.toMillis(false);
        mFirstJulianDay = Time.getJulianDay(start, mBaseDate.gmtoff);
        mLastJulianDay = mFirstJulianDay + mNumDays - 1;
        
        mMonthLength = mBaseDate.getActualMaximum(Time.MONTH_DAY);
        mFirstVisibleDate = mBaseDate.monthDay;
        mFirstVisibleDayOfWeek = mBaseDate.weekDay;
    }

    private int computeMaxStringWidth(int currentMax, String[] strings, Paint p) {
        float maxWidthF = 0.0f;

        int len = strings.length;
        for (int i = 0; i < len; i++) {
            float width = p.measureText(strings[i]);
            maxWidthF = Math.max(width, maxWidthF);
        }
        int maxWidth = (int) (maxWidthF + 0.5);
        if (maxWidth < currentMax) {
            maxWidth = currentMax;
        }
        return maxWidth;
    }

    private OverScroller mScroller;
    private int mViewStartY;
    private final ContinueScroll mContinueScroll = new ContinueScroll();

    private class ContinueScroll implements Runnable {

        public void run() {
//            mScrolling = mScrolling && mScroller.computeScrollOffset();
//            mViewStartY = mScroller.getCurrY();
//            mHandler.post(this);
//            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw");
        float yTranslate = -mViewStartY + 50;
        canvas.translate(-50, yTranslate);
        Rect dest = mDestRect;
        dest.top = (int) (mFirstCell - yTranslate);
        dest.bottom = (int) (mViewHeight - yTranslate);
        dest.left = 20;
        dest.right = mViewWidth;
        canvas.save();
        canvas.clipRect(dest);
        doDraw(canvas);
        canvas.restore();
        canvas.translate(20, -yTranslate);
        // Draw the fixed areas (that don't scroll) directly to the canvas.
        drawAfterScroll(canvas);
//        canvas.restore();
    }

    private final Paint mPaint = new Paint();
    /**
     * The height of the day names/numbers
     */
    private static int DAY_HEADER_HEIGHT = 45;

    private void drawAfterScroll(Canvas canvas) {
        Paint p = mPaint;
        Rect r = mRect;
        drawScrollLine(r, canvas, p);
        drawDayHeaderLoop(r, canvas, p);

    }

    private static int ONE_DAY_HEADER_HEIGHT = DAY_HEADER_HEIGHT;
    private int mFirstJulianDay;
    private int mLastJulianDay;

    private void drawDayHeaderLoop(Rect r, Canvas canvas, Paint p) {
        if (mNumDays == 1 && ONE_DAY_HEADER_HEIGHT == 0) {
            return;
        }
        int cell = mF
    }

    /**
     * Top of the scrollable region i.e. below date labels and all day events
     */
    private int mFirstCell;

    private void drawScrollLine(Rect r, Canvas canvas, Paint p) {
        final int right = mViewWidth;
        final int y = mFirstCell - 1;

        p.setAntiAlias(false);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        canvas.drawLine(20, y, right, y, p);
        p.setAntiAlias(true);
    }

    private void doDraw(Canvas canvas) {
        drawGridBg(mRect, canvas);
    }

    float mCellHeight = 193;
    float mCellWidth = 72;
    final int maxGridLines = (24 + 1) //max horizontal lines we might draw
            + (mNumDays + 1) + 1;//max vertical lines we might draw
    float[] mLines = new float[maxGridLines * 4];

    private void drawGridBg(Rect rect, Canvas canvas) {
        Paint paint = new Paint();
        float y = 100;
        float x = 100;

        final float deltaY = mCellHeight;//mCellHeight
        int linesIndex = 0;
        final float startX = x;
        final float startY = y;
        final float stopX = getWidth() - startX;
        final float stopY = 24 * mCellHeight;

        //draw the inner horizontal grid lines
        //绘制内部水平网格线
        paint.setColor(Color.RED);
        for (int hour = 0; hour <= 24; hour++) {
            mLines[linesIndex++] = x;
            mLines[linesIndex++] = y;
            mLines[linesIndex++] = stopX;
            mLines[linesIndex++] = y;
            y += deltaY;
        }
        canvas.drawLines(mLines, paint);

        linesIndex = 0;
        paint.setColor(Color.GREEN);
        //draw the inner vertical grid lines
        //绘制内部垂直网格线
        float effectiveWidth = getWidth() - startX;
        for (int day = 0; day <= mNumDays; day++) {
            x = day * effectiveWidth / mNumDays + startX;
            mLines[linesIndex++] = x;
            mLines[linesIndex++] = startY;
            mLines[linesIndex++] = x;
            mLines[linesIndex++] = stopY;
        }
        canvas.drawLines(mLines, paint);

    }

    private void drawBgColors(Rect rect, Canvas canvas) {

    }

    private void cancelAnimation() {
        Animation in = mViewSwitcher.getInAnimation();
        if (in != null) {
            // cancel() doesn't terminate cleanly.
            in.scaleCurrentDuration(0);
        }
        Animation out = mViewSwitcher.getOutAnimation();
        if (out != null) {
            // cancel() doesn't terminate cleanly.
            out.scaleCurrentDuration(0);
        }
    }

    private float getAverageY(MotionEvent me) {
        int count = me.getPointerCount();
        float focusY = 0;
        for (int i = 0; i < count; i++) {
            focusY += me.getY(i);
        }
        focusY /= count;
        return focusY;
    }

    private void doScroll(MotionEvent e1, MotionEvent e2, float deltaX, float deltaY) {
        cancelAnimation();
        if (mStartingScroll) {
            mInitialScrollX = 0;
            mInitialScrollY = 0;
            mStartingScroll = false;
        }

        mInitialScrollX += deltaX;
        mInitialScrollY += deltaY;
        int distanceX = (int) mInitialScrollX;
        int distanceY = (int) mInitialScrollY;
        invalidate();
        final float focusY = getAverageY(e2);
        mViewStartY = (int) (-focusY + 50);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                mStartingScroll = true;
                mGestureDetector.onTouchEvent(event);
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        int gridAreaWidth = w - 100;
//        mCellWidth =;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * The initial state of the touch mode when we enter this view.
     */
    private static final int TOUCH_MODE_INITIAL_STATE = 0;
    /**
     * Indicates we just received the touch event and we are waiting to see if
     * it is a tap or a scroll gesture.
     */
    private static final int TOUCH_MODE_DOWN = 1;
    private int mTouchMode = TOUCH_MODE_INITIAL_STATE;

    private void doDown(MotionEvent ev) {
        mTouchMode = TOUCH_MODE_DOWN;
    }

    private class CalendarGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            DayView.this.doDown(e);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            DayView.this.doScroll(e1, e2, distanceX, distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            DayView.this.doFling(e1, e2, velocityX, velocityY);
            return true;
        }
    }

    private Handler mHandler;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mHandler == null) {
            mHandler = getHandler();
//            mHandler.post(System.currentTimeMillis());
        }
    }

    private void doFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        cancelAnimation();
        mHandler.post(mContinueScroll);
    }

}
