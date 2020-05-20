package com.domyos.econnected.ui.view;/**
 * Created by HouWei on 16/8/5.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.domyos.econnected.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author HouWei
 * Date 16/8/5
 * Time 10:28
 * Package com.yuedong.apps.treadmill.ui.view
 */
public class HeartRateLineChartView extends View {

    public HeartRateLineChartView(Context context) {
        this(context, null);
    }

    public HeartRateLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartRateLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Paint mPaint;

    private static final int COLOR_RED = Color.parseColor("#FFFE805E");
    private static final int COLOR_BLACK = Color.parseColor("#000000");
    private static final int COLOR_ALPHA_RED = Color.parseColor("#80FE805E");
    private static final int COLOR_COORDINATE_BIG = Color.parseColor("#FF6D6D6D");
    private static final int COLOR_TEXT = Color.parseColor("#FF7D7D7D");
    private static final int COLOR_COORDINATE_SMALL = Color.parseColor("#FF7C7C7C");
    private static final int COLOR_WHITE = Color.parseColor("#ffffff");
    private static final int COLOR_ALPHA_WHITE = Color.parseColor("#80ffffff");
    private static final int COLOR_WARM_UP = Color.parseColor("#1DAEEB");
    private static final int COLOR_CALORIE_BURN = Color.parseColor("#8CC42B");
    private static final int COLOR_AEROBIC_ENDURANCE = Color.parseColor("#FEEF35");
    private static final int COLOR_ANAEROBIC_ENDURANCE = Color.parseColor("#F49231");
    private static final int COLOR_LIMIT = Color.parseColor("#E92B2D");

    private final int[] COLORS = new int[]{
            Color.parseColor("#DD8BBE31"), Color.parseColor("#DDF7E933"),
            Color.parseColor("#DDF3B23D"), Color.parseColor("#DDE45524")};

    private float width = 0;
    private float height = 0;

    private float yAxisWidth = 0;
    private float xAxisHeight = 0;

    private float coordinateLeft = 0;
    private float coordinateRight = 0;
    private float coordinateTop = 0;
    private float coordinateBottom = 0;

    private float lineLeft = 0;
    private float lineRight = 0;
    private float lineTop = 0;
    private float lineBottom = 0;

    private float textSize = 0;

    // x方向上 一秒的距离
    private float xStep = 0;
    // y方向上 一条分割线的距离
    private float yDividerStep = 0;
    //y方向上 等分200心率
    private float yStep = 0;
    private float pointSize = 0;
    private float lineWidth = 0;

    // 范围内最大心率
    private final float maxHeartRate = 200;
    // 范围内非零最小心率
    private final float minHeartRate = 0;

    private int[] heartRateDataArr;

    private final int SHOW_MINUTE = 1;

    // private int timeMinute = 0;
    // private int timeSecond = 0;
    private int runTime = 0;//单位秒


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    /**
     * 更新图表数据
     *
     * @param heartRateDataArr 传入时刻倒推65秒的心率数据  长度 0 - 60 * Show_minutes
     * @param runtime          心率连接时长
     */
    public void updateData(int[] heartRateDataArr, int runtime) {
        this.heartRateDataArr = heartRateDataArr;
        if (heartRateDataArr != null && heartRateDataArr.length > 0) {
            int max = heartRateDataArr[0];
            int min = heartRateDataArr[0];
            for (int i = 0; heartRateDataArr != null && i < heartRateDataArr.length; i++) {
                if (max < heartRateDataArr[i]) {
                    max = heartRateDataArr[i];
                }
                if (heartRateDataArr[i] > 0 && min > heartRateDataArr[i]) {
                    min = heartRateDataArr[i];
                }
            }
//            if (max == 0 && min == 0) {
//                this.maxHeartRate = 200;
//                this.minHeartRate = 50;
//            } else {
//                this.maxHeartRate = max;
//                this.minHeartRate = min;
//            }
            this.runTime = runtime;
            postInvalidate();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        maybeInit();
        // 绘制坐标系
        drawCoordinate(canvas);
        // drawXAxis(canvas);
        drawYAxis(canvas);
        drawLine(canvas);
        drawCurrentHeartRate(canvas);
    }


    private void drawLine(Canvas canvas) {
        Path path = new Path();
        List<Point> points = new ArrayList<>();
        if (heartRateDataArr != null && heartRateDataArr.length > 0) {
            //心跳最高200，把Y轴等分为200份
            final float step = (coordinateBottom - coordinateTop) / (maxHeartRate - minHeartRate);
            // 上一个点是否是零
            boolean zeroFlag = true;
            // 第一个非零点
            Point startPoint = new Point();
            // 最后一个非零点
            Point endPoint = new Point();
            for (int i = 0; i < heartRateDataArr.length; i++) {

                if (heartRateDataArr[i] >= minHeartRate) {
                    float x = lineLeft + (i * xStep);
                    float heartStep = heartRateDataArr[i] - minHeartRate-25;
                    if(heartStep<0){
                        heartStep = 0;
                    }
                    float y = coordinateBottom - heartStep * step;

                    Point point = new Point((int) x, (int) y);
                    if (zeroFlag) { // 上一个点是零，线段断开
                        path.moveTo(x, y);
                        // 第一个非零点
                        startPoint.set((int) x, (int) y);
                    } else { // 上一个点不是零，线段连续
                        path.lineTo(x, y);
                    }
                    if (i == heartRateDataArr.length - 1) {
                        // 最后一个点，默认绘制圆点
                        points.add(point);
                    } else if (points.size() <= 0) {
                        // 第一个点，默认绘制圆点
                        points.add(point);
                    } else if (heartRateDataArr[i + 1] != heartRateDataArr[i]) {
                        // 非最后一个点，数值发生变化时绘制圆点
                        points.add(point);
                    } else if (heartRateDataArr[i - 1] != heartRateDataArr[i]) {
                        // 非最后一个点，数值发生变化时绘制圆点
                        points.add(point);
                    }
                    // 最后一个点
                    endPoint.set(point.x, point.y);
                    zeroFlag = false;
                } else {
                    zeroFlag = true;
                    // 心跳为零，线段断开，进行绘制
                    if (!path.isEmpty()) {
                        // 画线
                        mPaint.setColor(COLOR_WHITE);
                        mPaint.setStyle(Paint.Style.STROKE);
                        mPaint.setStrokeWidth(lineWidth);
                        canvas.drawPath(path, mPaint);
                        // 补全半透明区域
                        path.lineTo(endPoint.x, coordinateBottom - lineWidth / 2);
                        path.lineTo(startPoint.x, coordinateBottom - lineWidth / 2);
                        path.close();
                        // 绘制半透明红色区域
                        mPaint.setColor(COLOR_ALPHA_WHITE);
                        mPaint.setStyle(Paint.Style.FILL);
                        canvas.drawPath(path, mPaint);
                        // 重置路径
                        path = new Path();
                        startPoint = new Point();
                        endPoint = new Point();
                    }
                }

            }

            // 画线
            if (!path.isEmpty()) {
                mPaint.setColor(COLOR_WHITE);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(lineWidth);
                canvas.drawPath(path, mPaint);
                // 补全半透明区域
                path.lineTo(endPoint.x, coordinateBottom - lineWidth / 2);
                path.lineTo(startPoint.x, coordinateBottom - lineWidth / 2);
                path.close();

                // 绘制半透明红色区域
                mPaint.setColor(COLOR_ALPHA_WHITE);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawPath(path, mPaint);
            }

            mPaint.setColor(COLOR_ALPHA_WHITE);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

            // 画点

//            for (Point point : points) {
//                canvas.drawCircle(point.x, point.y, pointSize, mPaint);
//            }


            // 绘制透明红色区域

        }

    }

    private void drawYAxis(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            // 绘制水平分割线
            mPaint.setStrokeWidth(AutoUtils.getPercentHeightSize(1));
            mPaint.setColor(COLOR_COORDINATE_SMALL);
            mPaint.setStyle(Paint.Style.STROKE);
            float y = lineBottom - i * yDividerStep;
            canvas.drawLine(coordinateLeft, y, coordinateRight, y, mPaint);

            // 绘制文字
            mPaint.setTextSize(textSize);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(COLOR_WHITE);
            mPaint.setStrokeWidth(AutoUtils.getPercentHeightSize(1));

            RectF targetRect = new RectF(0, y - textSize, coordinateLeft, y + textSize);
            //String text = (int) (i * ((maxHeartRate - minHeartRate) / 4) + minHeartRate) + "";
            String text = String.valueOf(60 + i * 30);
            drawText(text, targetRect, mPaint, canvas);

            //绘制背景
//            mPaint.setStrokeWidth(yDividerStep);
//            mPaint.setColor(COLORS[i]);
//            mPaint.setStyle(Paint.Style.STROKE);
//            y = coordinateBottom - i * yDividerStep - yDividerStep / 2;
//            canvas.drawLine(coordinateLeft, y, coordinateLeft + 10, y, mPaint);
        }
    }

    private void drawText(String text, RectF targetRect, Paint paint, Canvas canvas) {
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        float baseLine = (targetRect.bottom + targetRect.top - fontMetricsInt.bottom - fontMetricsInt.top) / 2;
        canvas.drawText(text, targetRect.centerX(), baseLine, paint);
    }

    private void drawXAxis(Canvas canvas) {
        // 绘制文字
//        mPaint.setTextAlign(Paint.Align.CENTER);
//        mPaint.setColor(COLOR_TEXT);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(AutoUtils.getPercentHeightSize(1));
//        mPaint.setTextSize(textSize);
//
//        if (timeMinute >= SHOW_MINUTE) {
//            String leftText = String.format("%02d:%02d", (timeMinute - 1 * SHOW_MINUTE), timeSecond);
//            String rightText = String.format("%02d:%02d", timeMinute, timeSecond);
//            float leftTextCenterX = lineLeft + 5 * xStep;
//
//            RectF leftTargetRectF = new RectF(leftTextCenterX - 4 * textSize, coordinateBottom, leftTextCenterX + 4 * textSize, height);
//            drawText(leftText, leftTargetRectF, mPaint, canvas);
//
//            float rightTextCenterX = lineLeft + SHOW_MINUTE * 60 * xStep;
//            RectF rightTargetRectF = new RectF(rightTextCenterX - 4 * textSize, coordinateBottom, rightTextCenterX + 4 * textSize, height);
//            drawText(rightText, rightTargetRectF, mPaint, canvas);
//
//        } else {
//            // 不够十分钟
//            //String leftText = String.format("%02d:%02d", 0, 5);
//            String rightText = String.format("%02d:%02d", timeMinute, timeSecond);
//            float rightTextCenterX = lineLeft + (timeMinute * 60 + timeSecond) * xStep;
//            RectF rightTargetRectF = new RectF(rightTextCenterX - 4 * textSize, coordinateBottom, rightTextCenterX + 4 * textSize, height);
//            drawText(rightText, rightTargetRectF, mPaint, canvas);
//
//
//        }
    }

    private void drawCoordinate(Canvas canvas) {
        mPaint.setColor(COLOR_COORDINATE_BIG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);

        // 画线
        canvas.drawLine(coordinateLeft, coordinateBottom, coordinateRight, coordinateBottom, mPaint);
        canvas.drawLine(coordinateLeft, coordinateTop, coordinateLeft, coordinateBottom, mPaint);

        // 画点
        canvas.drawCircle(coordinateLeft, coordinateBottom, pointSize, mPaint);
        canvas.drawCircle(coordinateLeft, coordinateTop, pointSize, mPaint);
        canvas.drawCircle(coordinateRight, coordinateBottom, pointSize, mPaint);


    }

    private void drawCurrentHeartRate(Canvas canvas) {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(COLOR_WARM_UP);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(AutoUtils.getPercentHeightSize(1));
        mPaint.setTextSize(textSize);

        int heartRate = getHeartRate(heartRateDataArr);
        if (heartRate == 0) return;

        float right = lineLeft + runTime * xStep + textSize * 2;
        float left = lineLeft + runTime * xStep;

        if (runTime >= SHOW_MINUTE * 60) {
            right = lineLeft + SHOW_MINUTE * 60 * xStep + textSize * 2;
            left = lineLeft + SHOW_MINUTE * 60 * xStep;
        }

        float bottom = coordinateBottom - heartRate * yStep+48;
        float top = coordinateBottom - heartRate * yStep - textSize+48;

        String text = "";
        if (heartRate >= 60 && heartRate < 90) {
            //热身
            mPaint.setColor(COLOR_WARM_UP);
            text = getContext().getString(R.string.heart_warmup);
        } else if (heartRate >= 90 && heartRate < 120) {    
            //燃脂
            mPaint.setColor(COLOR_CALORIE_BURN);
            text =  getContext().getString(R.string.heart_fatburm);
        } else if (heartRate >= 120 && heartRate < 150) {
            //有氧耐力
            mPaint.setColor(COLOR_AEROBIC_ENDURANCE);
            text =  getContext().getString(R.string.heart_youyang);
        } else if (heartRate >= 150 && heartRate < 180) {
            //无氧耐力
            mPaint.setColor(COLOR_ANAEROBIC_ENDURANCE);
            text =  getContext().getString(R.string.heart_wuyang);
        } else if (heartRate > 180) {
            //极限
            mPaint.setColor(COLOR_LIMIT);
            text =  getContext().getString(R.string.heart_limit);
        }

        mPaint.setStrokeWidth(textSize / 8f);
        if (runTime >= 60) {
            canvas.drawCircle(left - textSize / 2f, bottom, textSize / 2f, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(left - textSize / 2f, bottom, textSize / 4f, mPaint);
        } else {
            canvas.drawCircle(left - textSize / 2f, bottom, textSize / 2f, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(left - textSize / 2f, bottom, textSize / 4f, mPaint);
        }


        RectF bgRectF = new RectF(left + textSize / 2f, top - textSize / 2 + textSize / 3, left + textSize * 4.5f, bottom + textSize / 2 + textSize / 3);
        canvas.drawRoundRect(bgRectF, textSize / 2, textSize / 2, mPaint);
        mPaint.setColor(COLOR_BLACK);
        RectF textRectF = new RectF(left + textSize * 1.5f, (top + bottom - textSize) / 2 + textSize / 3f, left + textSize * 3.5f, (bottom + top + textSize) / 2 + textSize / 3f);
        drawText(text, textRectF, mPaint, canvas);
    }

    private void maybeInit() {
        if (width > 0) {
            return;
        }
        width = getWidth();
        height = getHeight();

        // 文字大小默认 15px
        textSize = AutoUtils.getPercentHeightSize(20);

        xAxisHeight = textSize + AutoUtils.getPercentHeightSize(9) * 2;
        yAxisWidth = textSize * 3 + AutoUtils.getPercentWidthSize(10) * 2;

        coordinateLeft = yAxisWidth;
        coordinateRight = width - textSize;
        coordinateTop = 0 + textSize;
        coordinateBottom = height - xAxisHeight;

        // 一共长度75秒  左边留5秒 右边不画10秒,总共显示10分钟
        xStep = (coordinateRight - coordinateLeft) / 70.0f / SHOW_MINUTE;
        yStep = (coordinateBottom - coordinateTop) / 200;
        yDividerStep = (coordinateBottom - coordinateTop) / 6.0f;


        lineLeft = coordinateLeft;
        lineTop = coordinateTop + yDividerStep;
        lineRight = coordinateRight - xStep * 10;
        lineBottom = coordinateBottom - yDividerStep;


        pointSize = AutoUtils.getPercentHeightSize(1);
        lineWidth = AutoUtils.getPercentHeightSize(3);

    }

    private int getHeartRate(int[] heartRateDataArr) {
        int heartRate = 0;
        if (heartRateDataArr != null) {
            if (heartRateDataArr.length > 1) {
                heartRate = heartRateDataArr[heartRateDataArr.length - 1];
            } else if (heartRateDataArr.length == 1) {
                heartRate = heartRateDataArr[0];
            }
            return heartRate;
        }
        return 0;
    }


}
