package com.domyos.econnected.ui.view.svg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.domyos.econnected.R;
import com.domyos.econnected.constant.UserPicConstant;

/**
 * Created by yanfa-35 on 2017/8/1.
 */

public class RunWayView extends View {
    private int imageWidth = 1760, realityWidth;
    private int imageHeight = 1064, realityHeight;
    private int distance = 0;// 单位：m
    private String[] mGlyphStrings;
    private GlyphData[] mGlyphData;
    private float pathLength;
    private Path normalPath;
    private float[] mCurrentPosition = new float[2];
    private PathMeasure mPathMeasure;
    private int runwayLength;//一圈的长度
    private PointF distance0Point = new PointF();
    private PointF distance1Point = new PointF();
    private PointF distance2Point = new PointF();
    private PointF distance3Point = new PointF();
    private PointF distance4Point = new PointF();
    private boolean drawInterval = false;//是否绘制跑道分隔线
    //用户名称
    private String[] userNames;
    public float[] getMyDistances() {
        return myDistances;
    }

    public void setMyDistances(float[] myDistances) {
        this.myDistances = myDistances;
    }

    //TODO TEST
    private float[] myDistances;

    public int[] getResID() {
        return resID;
    }

    public void setResID(int[] resID) {
        this.resID = resID;
    }

    private int[] resID;


    public RunWayView(Context context) {
        super(context);

    }

    public RunWayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public RunWayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Path circlePath;
    private Paint circlePaint;
    private Paint normalPaint;
    private Paint textPaint;

    private Path progressPath;
    private Paint progressPaint;
    private Bitmap bitmap;
    private BitmapFactory.Options options;
    private Context context;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rebuildGlyphData(mGlyphStrings);
        circlePath = new Path();
        circlePaint = new Paint();
        circlePaint.setColor(getResources().getColor(R.color.brown));//圆点颜色
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

        normalPaint = new Paint();
        normalPaint.setColor(getResources().getColor(R.color.brown));
        normalPaint.setStyle(Paint.Style.STROKE);
        normalPaint.setStrokeWidth(10);
        normalPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(getResources().getColor(R.color.brown));
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(41);
        progressPaint.setAntiAlias(true);
        canvas.drawPath(normalPath, normalPaint);
        Path dst = new Path();
        dst.rLineTo(0, 0);
        if (mPathMeasure.getSegment(0, distance * (pathLength / runwayLength), dst, true)) {
            //绘制线
            //canvas.drawPath(dst, progressPaint);
        }


        if (myDistances !=null  && myDistances.length > 0) {
            for (int i = 0; i < myDistances.length; i++) {
                mPathMeasure.getPosTan(myDistances[i] * (pathLength / runwayLength), mCurrentPosition, null);

                //圆点的半径
                circlePath.addCircle(mCurrentPosition[0], mCurrentPosition[1], 20, Path.Direction.CW);
                canvas.drawPath(circlePath, circlePaint);


                //图片大小设置，我这里设置的是60,mCurrentPosition是图片的位置，可以通过这个调整
                //选择头像

                if (resID[i] == 0) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }

                if (resID[i] == UserPicConstant.TYPE_01_01) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }
                if (resID[i] == UserPicConstant.TYPE_01_02) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }
                if (resID[i] == UserPicConstant.TYPE_01_03) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);
                   /* canvas.drawBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.pic_01_03, 40, 40),
                            mCurrentPosition[0]+10, mCurrentPosition[1], circlePaint);*/
                }
                if (resID[i] == UserPicConstant.TYPE_02_01) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }
                if (resID[i] == UserPicConstant.TYPE_02_02) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }
                if (resID[i] == UserPicConstant.TYPE_02_03) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }

                if (resID[i] == UserPicConstant.TYPE_03_01) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }
                if (resID[i] == UserPicConstant.TYPE_03_02) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }
                if (resID[i] == UserPicConstant.TYPE_03_03) {
                    canvas.drawBitmap( getViewBitmap(context,userNames[i],resID[i]),mCurrentPosition[0]-40, mCurrentPosition[1], circlePaint);

                }


            }
        }
//        mPathMeasure.getPosTan(distance * (pathLength / runwayLength), mCurrentPosition, null);
//        circlePath.addCircle(mCurrentPosition[0], mCurrentPosition[1], 20, Path.Direction.CW);
//        canvas.drawPath(circlePath, circlePaint);
//        if (drawInterval) {
//            drawInterval(canvas, mPathMeasure);
//        }
        //canvas.drawText("400 m", realityWidth / 2, realityHeight / 2, textPaint);
    }

    private void rebuildGlyphData(String[] pathData) {
        realityWidth = getWidth();
        realityHeight = getHeight();
        mGlyphStrings = pathData;
        SvgPathToAndroidPath.setScale(realityWidth / (float) imageWidth, realityHeight / (float) imageHeight);
        mGlyphData = new GlyphData[mGlyphStrings.length];
        for (int i = 0; i < mGlyphStrings.length; i++) {
            mGlyphData[i] = new GlyphData();
            mGlyphData[i].path = SvgPathToAndroidPath.parser(mGlyphStrings[i]);
            PathMeasure pm = new PathMeasure(mGlyphData[i].path, true);
            while (true) {
                mGlyphData[i].length = Math.max(mGlyphData[i].length, pm.getLength());
                if (!pm.nextContour()) {
                    break;
                }
            }
        }
        for (int i = 0; i < mGlyphData.length; i++) {
            normalPath = mGlyphData[i].path;
            mPathMeasure = new PathMeasure(normalPath, false);
            pathLength = mGlyphData[i].length;
        }
    }

    public void drawInterval(Canvas canvas, PathMeasure mPathMeasure) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(40);
        paint.setAntiAlias(true);
        float[] distancePosition = new float[2];
        mPathMeasure.getPosTan(0 * (pathLength / 5000), distancePosition, null);
        distance0Point.set(distancePosition[0], distancePosition[1]);
        mPathMeasure.getPosTan(1000 * (pathLength / 5000), distancePosition, null);
        distance1Point.set(distancePosition[0], distancePosition[1]);
        mPathMeasure.getPosTan(2000 * (pathLength / 5000), distancePosition, null);
        distance2Point.set(distancePosition[0], distancePosition[1]);
        mPathMeasure.getPosTan(3000 * (pathLength / 5000), distancePosition, null);
        distance3Point.set(distancePosition[0], distancePosition[1]);
        mPathMeasure.getPosTan(4000 * (pathLength / 5000), distancePosition, null);
        distance4Point.set(distancePosition[0], distancePosition[1]);

        mPathMeasure.getPosTan(10 * (pathLength / 5000), distancePosition, null);
        distance01Point.set(distancePosition[0], distancePosition[1]);
        mPathMeasure.getPosTan(1010 * (pathLength / 5000), distancePosition, null);
        distance11Point.set(distancePosition[0], distancePosition[1]);
        mPathMeasure.getPosTan(2010 * (pathLength / 5000), distancePosition, null);
        distance21Point.set(distancePosition[0], distancePosition[1]);
        mPathMeasure.getPosTan(3010 * (pathLength / 5000), distancePosition, null);
        distance31Point.set(distancePosition[0], distancePosition[1]);
        mPathMeasure.getPosTan(4010 * (pathLength / 5000), distancePosition, null);
        distance41Point.set(distancePosition[0], distancePosition[1]);
        //canvas.drawLine(distance0Point.x, distance0Point.y, distance01Point.x, distance01Point.y, paint);
        canvas.drawLine(distance1Point.x, distance1Point.y, distance11Point.x, distance11Point.y, paint);
        canvas.drawLine(distance2Point.x, distance2Point.y, distance21Point.x, distance21Point.y, paint);
        canvas.drawLine(distance3Point.x, distance3Point.y, distance31Point.x, distance31Point.y, paint);
        canvas.drawLine(distance4Point.x, distance4Point.y, distance41Point.x, distance41Point.y, paint);
    }

    private PointF distance01Point = new PointF();
    private PointF distance11Point = new PointF();
    private PointF distance21Point = new PointF();
    private PointF distance31Point = new PointF();
    private PointF distance41Point = new PointF();

    public void setProgress(float[] myDistances, int[] resID, String[] userNames) {
        for (int i = 0; i < myDistances.length; i++) {
            if (myDistances[i] > runwayLength) {
                myDistances[i] = myDistances[i] - (myDistances[i] / runwayLength) * runwayLength;
            }
        }
        this.myDistances = myDistances;
        this.resID = resID;
        this.userNames = userNames;
        invalidate();
    }

    // public void set
    public void setGlyphStrings(String[] glyphStrings) {
        mGlyphStrings = glyphStrings;
    }

    private static class GlyphData {
        Path path;
        float length;
    }

    public int getRunwayLength() {
        return runwayLength;
    }

    public void setRunwayLength(int runwayLength) {
        this.runwayLength = runwayLength;
    }

    /**
     * use this view ,first need to set svg data
     *
     * @param pathData
     */
    public void setmGlyphStrings(String[] pathData,Context context) {
        this.context = context;
        this.mGlyphStrings = pathData;
        options = new BitmapFactory.Options();
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;//压缩画质
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    //设置移动的view头像名字
    public Bitmap getViewBitmap(Context context,String name,int picId) {
        View view = LayoutInflater.from(context).inflate(R.layout.race_touxiang_layout, null);
        int me = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        TextView textView = view.findViewById(R.id.race_touxiang_name);
        textView.setText(name);
        ImageView imageView = view.findViewById(R.id.race_touxiang_img);

        if (picId == 0) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.touxiang_img));

        }
        if (picId == UserPicConstant.TYPE_01_01) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_01_01));

        }

        if (picId == UserPicConstant.TYPE_01_02) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_01_02));

        }

        if (picId == UserPicConstant.TYPE_01_03) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_01_03));

        }
        if (picId == UserPicConstant.TYPE_02_01) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_02_01));

        }

        if (picId == UserPicConstant.TYPE_02_02) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_02_02));

        }

        if (picId == UserPicConstant.TYPE_02_03) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_02_03));

        }
        if (picId == UserPicConstant.TYPE_03_01) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_03_01));

        }

        if (picId == UserPicConstant.TYPE_03_02) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_03_02));

        }

        if (picId == UserPicConstant.TYPE_03_03) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.pic_03_03));

        }






        view.measure(me, me);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return  bitmap;
    }
}
