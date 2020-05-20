package com.domyos.econnected.ui.view;/**
 * Created by HouWei on 16/8/11.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Author HouWei
 * Date 16/8/11
 * Time 15:07
 * Package com.yuedong.apps.treadmill.ui.view
 */

public class ScaleView extends View {
    /**
     * 刻度为0.5
     */
    public static final int MOD_TYPE_HALF = 2;
    /**
     * 刻度为5
     */
    public static final int MOD_TYPE_FIVE = 5;
    /**
     * 刻度为10
     */
    public static final int MOD_TYPE_ONE = 10;
    private static int ITEM_HALF_DIVIDER = 20;
    private static int ITEM_ONE_DIVIDER = 20;
    private static int ITEM_MAX_HEIGHT = 30;
    private static int ITEM_MIN_HEIGHT = 20;
    private static int STROKE_WIDTH = 7;
    private static int TEXT_SIZE = 34;
    private Paint paint;
    private TextPaint textPaint;
    private int cursorColor = Color.parseColor("#40A8DD");
    private float cursorH;
    private float mDensity;
    private int mValue = 50, mMaxValue = 100, mMinValue = 0, mModType = MOD_TYPE_ONE, mLineDivider = ITEM_ONE_DIVIDER;
    private int mLastX, mMove;
    private int mWidth, mHeight;
    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private boolean mDrawTopScale = true;
    private boolean mDrawBottomScale = true;
    private OnValueChangeListener mListener;

    @SuppressWarnings("deprecation")
    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        cursorH = mDensity * 1;

        autoLayout();

//        cursorH = mDensity * 12;
        super.onLayout(changed, left, top, right, bottom);
    }

    private void autoLayout() {
//        ITEM_HALF_DIVIDER = AutoUtils.getPercentHeightSize(20);
//        ITEM_ONE_DIVIDER = AutoUtils.getPercentHeightSize(20);
//        ITEM_MAX_HEIGHT = AutoUtils.getPercentHeightSize(30);
//        ITEM_MIN_HEIGHT = AutoUtils.getPercentHeightSize(20);
//        TEXT_SIZE = AutoUtils.getPercentHeightSize(20);
//        STROKE_WIDTH = AutoUtils.getPercentWidthSize(7);
    }

    public boolean isDrawBottomScale() {
        return mDrawBottomScale;
    }

    public void setDrawBottomScale(boolean drawBottomScale) {
        mDrawBottomScale = drawBottomScale;
        postInvalidate();
    }

    public boolean isDrawTopScale() {
        return mDrawTopScale;
    }

    public void setDrawTopScale(boolean drawTopScale) {
        mDrawTopScale = drawTopScale;
        postInvalidate();
    }

    private void init() {


        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density;
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

        paint = new Paint();
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setAntiAlias(true);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(TEXT_SIZE * mDensity);
        textPaint.setColor(Color.parseColor("#FF6A6A6A"));
    }

    /**
     * @param defaultValue 初始值
     * @param maxValue     最大值
     * @param model        刻度盘精度 MOD_TYPE_HALF,MOD_TYPE_ONE,MOD_TYPE_FIVE，三种
     */
    public void initViewParam(int defaultValue, int maxValue, int minValue, int model) {
        switch (model) {
            case MOD_TYPE_HALF:
                mModType = MOD_TYPE_HALF;
                mLineDivider = ITEM_HALF_DIVIDER;
                mValue = defaultValue * 2;
                mMaxValue = maxValue * 2;
                mMinValue = minValue * 2;

                break;
            case MOD_TYPE_ONE:
                mModType = MOD_TYPE_ONE;
                mLineDivider = ITEM_ONE_DIVIDER;
                mValue = defaultValue;
                mMaxValue = maxValue;
                mMinValue = minValue;
                break;
            case MOD_TYPE_FIVE:
                mModType = MOD_TYPE_FIVE;
                mLineDivider = ITEM_ONE_DIVIDER;
                mValue = defaultValue;
                mMaxValue = maxValue;
                mMinValue = minValue;
            default:
                break;
        }
        invalidate();

        mLastX = 0;
        mMove = 0;
        notifyValueChange();
    }

    /**
     * 设置用于接收结果的监听器
     *
     * @param listener
     */
    public void setValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    /**
     * 获取当前刻度值
     *
     * @return
     */
    public float getValue() {
        return mValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#FF333333"));
        paint.setStrokeWidth(STROKE_WIDTH);

//        canvas.drawLine(0, cursorH, mWidth, cursorH, paint);

        drawScaleLine(canvas);
//        drawMiddleLine(canvas);

//        drawCursor(canvas);
    }

    /**
     * 画上边的小三角
     *
     * @param canvas
     */
    private void drawCursor(Canvas canvas) {
        Path path = new Path();
        float centerX = mWidth / 2.0f;
        paint.setColor(cursorColor);

        path.moveTo(centerX - mDensity * 9, 0);
        path.lineTo(centerX, cursorH);
        path.lineTo(centerX + mDensity * 9, 0);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 从中间往两边开始画刻度线
     *
     * @param canvas
     */
    private void drawScaleLine(Canvas canvas) {
        canvas.save();
        // 文本的baseline
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        float baseLine = (mHeight - fontMetricsInt.bottom - fontMetricsInt.top) / 2;

        paint.setColor(Color.parseColor("#FF333333"));

        int width = mWidth, drawCount = 0;

        float xPosition = 0;
        float textWidth = Layout.getDesiredWidth("0", textPaint);

        for (int i = 0; drawCount <= 4 * width; i++) {
            int numSize = ("" + (mValue + i)).length();

            xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
            if (xPosition + getPaddingRight() < mWidth) {

                if ((mValue + i) % mModType == 0) {
                    if (mDrawTopScale) {
                        canvas.drawLine(xPosition, cursorH, xPosition, cursorH + mDensity * ITEM_MAX_HEIGHT, paint);
                    }
                    if (mDrawBottomScale) {
                        canvas.drawLine(xPosition, mHeight, xPosition, mHeight - mDensity * ITEM_MAX_HEIGHT, paint);
                    }
                    //画刻度文字
                    if (mValue + i <= mMaxValue) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(String.valueOf((mValue + i) / 2), countLeftStart(mValue + i, xPosition, textWidth), baseLine, textPaint);
                                break;
                            case MOD_TYPE_FIVE:
                            case MOD_TYPE_ONE:
                                canvas.drawText(String.valueOf(mValue + i), xPosition - (textWidth * numSize / 2), baseLine, textPaint);
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    if (mDrawTopScale) {
                        canvas.drawLine(xPosition, cursorH, xPosition, cursorH + mDensity * ITEM_MIN_HEIGHT, paint);
                    }
                    if (mDrawBottomScale) {
                        canvas.drawLine(xPosition, mHeight, xPosition, mHeight - mDensity * ITEM_MIN_HEIGHT, paint);
                    }
                }
            }

            xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
            if (xPosition > getPaddingLeft()) {
                if ((mValue - i) % mModType == 0) {
                    if (mDrawTopScale) {
                        canvas.drawLine(xPosition, cursorH, xPosition, cursorH + mDensity * ITEM_MAX_HEIGHT, paint);
                    }
                    if (mDrawBottomScale) {
                        canvas.drawLine(xPosition, mHeight, xPosition, mHeight - mDensity * ITEM_MAX_HEIGHT, paint);
                    }

                    //画刻度文字
                    if (mValue - i >= mMinValue) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(String.valueOf((mValue - i) / 2), countLeftStart(mValue - i, xPosition, textWidth), baseLine, textPaint);
                                break;
                            case MOD_TYPE_FIVE:
                            case MOD_TYPE_ONE:
                                canvas.drawText(String.valueOf(mValue - i), xPosition - (textWidth * numSize / 2), baseLine, textPaint);
                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    if (mDrawTopScale) {
                        canvas.drawLine(xPosition, cursorH, xPosition, cursorH + mDensity * ITEM_MIN_HEIGHT, paint);
                    }
                    if (mDrawBottomScale) {
                        canvas.drawLine(xPosition, mHeight, xPosition, mHeight - mDensity * ITEM_MIN_HEIGHT, paint);
                    }
                }
            }

            drawCount += 2 * mLineDivider * mDensity;
        }

        canvas.restore();
    }

    /**
     * 计算没有数字显示位置的辅助方法
     *
     * @param value
     * @param xPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float xPosition, float textWidth) {
        float xp = 0f;
        if (value < 20) {
            xp = xPosition - (textWidth * 1 / 2);
        } else {
            xp = xPosition - (textWidth * 2 / 2);
        }
        return xp;
    }

    /**
     * 画中间的指示线
     *
     * @param canvas
     */
    private void drawMiddleLine(Canvas canvas) {
        canvas.save();
        paint.setColor(cursorColor);
        if (mValue % mModType == 0)
            canvas.drawLine(mWidth / 2, cursorH, mWidth / 2, cursorH + mDensity * ITEM_MAX_HEIGHT, paint);
        else
            canvas.drawLine(mWidth / 2, cursorH, mWidth / 2, cursorH + mDensity * ITEM_MIN_HEIGHT, paint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mScroller.forceFinished(true);

                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastX - xPosition) / 2;
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker(event);
                return false;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void countVelocityTracker(MotionEvent event) {
        mVelocityTracker.computeCurrentVelocity(500);
        float xVelocity = mVelocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    private void changeMoveAndValue() {
        int tValue = (int) (mMove / (mLineDivider * mDensity));
        if (Math.abs(tValue) > 0) {
            mValue += tValue;
            mMove -= tValue * mLineDivider * mDensity;
            if (mValue > mMaxValue) {
                mValue = mMaxValue;
                mMove = 0;
                mScroller.forceFinished(true);
            }
            if (mValue < mMinValue) {
                mValue = mMinValue;
                mMove = 0;
                mScroller.forceFinished(true);
            }
            notifyValueChange();
        }
        postInvalidate();
    }

    private void countMoveEnd() {
        int roundMove = Math.round(mMove / (mLineDivider * mDensity));
        mValue = mValue + roundMove;
        mValue = mValue <= 0 ? 0 : mValue;
        mValue = mValue > mMaxValue ? mMaxValue : mValue;
        mValue = mValue < mMinValue ? mMinValue : mValue;

        mLastX = 0;
        mMove = 0;

        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            if (mModType == MOD_TYPE_ONE || mModType == MOD_TYPE_FIVE) {
                mListener.onValueChange(mValue);
            }
            if (mModType == MOD_TYPE_HALF) {
                mListener.onValueChange(mValue / 2f);
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX()) { // over
                countMoveEnd();
            } else {
                int xPosition = mScroller.getCurrX();
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }

    public interface OnValueChangeListener {
        public void onValueChange(float value);
    }
}

