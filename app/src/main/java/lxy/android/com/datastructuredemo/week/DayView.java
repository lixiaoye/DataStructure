package lxy.android.com.datastructuredemo.week;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ViewSwitcher;

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


    public DayView(Context context) {
        this(context, null);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DayView(Context activity, ViewSwitcher mViewSwitcher, int numDays) {
        super(activity);
        mContext = activity;
        mNumDays = numDays;
        init(activity);
    }

    private void init(Context activity) {
        mCurrentTime = new Time();
        long currentTimeMillis = System.currentTimeMillis();
        mCurrentTime.set(currentTimeMillis);
        mTodayJulianDay = Time.getJulianDay(currentTimeMillis, mCurrentTime.gmtoff);
        Log.e(TAG, "DayView构造函数");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw");
        remeasure(getWidth(), getHeight());
        doDraw(canvas);
        /*Rect rect = mDestRect;
        rect.left = 100;
        rect.top = 100;
        rect.right = getWidth();
        rect.bottom = getHeight();
        canvas.save();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
//        canvas.drawRect(rect, paint);
//        canvas.restore();
        canvas.clipRect(rect);*/
    }

    private void doDraw(Canvas canvas) {
        drawBgColors(mRect, canvas);
        drawGridBg(mRect, canvas);
    }

    float mCellHeight = 193;
    float mCellWidth = 72;
    final int maxGridLines = (24 + 1) //max horizontal lines we might draw
            + (mNumDays + 1) + 1;//max vertical lines we might draw
    float[] mLines = new float[maxGridLines * 4];

    private void drawGridBg(Rect mRect, Canvas canvas) {
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

    private void remeasure(int width, int height) {
        for (int day = 0; day < mNumDays; day++) {

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        int gridAreaWidth = w - 100;
        mCellWidth =;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    //    private int measureHeight(int measureSpec) {
//        int result = 0;
//        int mode = MeasureSpec.getMode(measureSpec);
//        int size = MeasureSpec.getSize(measureSpec);
//
//        if (mode == MeasureSpec.EXACTLY) {
//            result = size;
//        } else {
//            if (mode == MeasureSpec.AT_MOST) {
//                result = Math.min(result, size);
//            }
//        }
//        return result;
//
//    }
//
//    private int measureWidth(int measureSpec) {
//        int result = 0;
//        int mode = MeasureSpec.getMode(measureSpec);
//        int size = MeasureSpec.getSize(measureSpec);
//
//        if (mode == MeasureSpec.EXACTLY) {
//            result = size;
//        } else {
//            if (mode == MeasureSpec.AT_MOST) {
//                result = Math.min(result, size);
//            }
//        }
//        return result;
//
//    }
}
