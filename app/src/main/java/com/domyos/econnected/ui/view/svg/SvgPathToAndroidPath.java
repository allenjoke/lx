package com.domyos.econnected.ui.view.svg;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * svg转化Android的工具类
 */

public class SvgPathToAndroidPath {
    private static int svgPathLenght = 0;
    private static String svgPath = null;
    private static int mIndex;
    private static List<Integer> cmdPositions = new ArrayList<>();
    private static float scaleX=1f,scaleY=1f;

    public static void setScale(float scaleX,float scaleY){
        SvgPathToAndroidPath.scaleX=scaleX;
        SvgPathToAndroidPath.scaleY=scaleY;
    }
    /**
     * M x,y  移动指令 映射path中moveTo
     * L x,y  画直线指令 映射path中lineTo
     * H x    画水平线指令  映射path中lineTo 要使用更上一个坐标的y
     * V y   画垂直线指令   映射path中lineTo 要使用更上一个坐标的x
     * C x1,y1,x2,y2,x,y   三次贝塞尔曲线指令 映射path中cubicTo
     * Q x1,y1,x,y         二次贝塞尔曲线指令 映射path中quadTo
     * S x2,y2,x,y    跟在C指令后面使用，用C指令的结束点做控制点，映射path中cubicTo
     * T x,y    跟在Q指令后面使用  使用Q的x,y 做控制点，映射path中quadTo
     * Z path关闭指令，映射close
     * A 用来画弧
     * */
    public static Path parser(String svgPath1) {
        svgPath = svgPath1;
        svgPathLenght = svgPath.length();
        mIndex = 0;
        Path lPath = new Path();
        lPath.setFillType(Path.FillType.WINDING);
        //记录最后一个操作点
        PointF lastPoint = new PointF();
        findCommand();
        for (int i = 0; i < cmdPositions.size(); i++) {
            Integer index = cmdPositions.get(i);
            switch (svgPath.charAt(index)) {
                case 'm':
                case 'M': {
                    String ps[] = findPoints(i);
                    lastPoint.set(Float.parseFloat(ps[0]), Float.parseFloat(ps[1]));
                    lPath.moveTo(lastPoint.x*scaleX, lastPoint.y*scaleY);
                }
                break;
                case 'l':
                case 'L': {
                    String ps[] = findPoints(i);
                    lastPoint.set(Float.parseFloat(ps[0]), Float.parseFloat(ps[1]));
                    lPath.lineTo(lastPoint.x, lastPoint.y);
                }
                break;
                case 'h':
                case 'H': {//基于上个坐标在水平方向上划线，因此y轴不变
                    String ps[] = findPoints(i);
                    lastPoint.set(Float.parseFloat(ps[0]), lastPoint.y);
                    lPath.lineTo(lastPoint.x, lastPoint.y);
                }
                break;
                case 'v':
                case 'V': {//基于上个坐标在水平方向上划线，因此x轴不变
                    String ps[] = findPoints(i);
                    lastPoint.set(lastPoint.x, Float.parseFloat(ps[0]));
                    lPath.lineTo(lastPoint.x, lastPoint.y);
                }
                break;
                case 'c':
                case 'C': {//3次贝塞尔曲线
                    List<String> list=findCPoints(i);
                    for (int j=0;j<list.size();j+=3){
                        PointF pointF1=new PointF(Float.parseFloat(list.get(j).split(",")[0]), Float.parseFloat(list.get(j).split(",")[1]));
                        PointF pointF2=new PointF(Float.parseFloat(list.get(j+1).split(",")[0]), Float.parseFloat(list.get(j+1).split(",")[1]));
                        PointF pointF3=new PointF(Float.parseFloat(list.get(j+2).split(",")[0]), Float.parseFloat(list.get(j+2).split(",")[1]));
                        lPath.cubicTo(pointF1.x*scaleX,pointF1.y*scaleY,pointF2.x*scaleX,pointF2.y*scaleY,pointF3.x*scaleX,pointF3.y*scaleY);
                        lastPoint.set(pointF3.x*scaleX, pointF3.y*scaleY);
                    }
                }
                break;
                case 's':
                case 'S': {//一般S会跟在C或是S命令后面使用，用前一个点做起始控制点
                    String ps[] = findPoints(i);
                    lPath.cubicTo(lastPoint.x,lastPoint.y, Float.parseFloat(ps[0]), Float.parseFloat(ps[1]), Float.parseFloat(ps[2]), Float.parseFloat(ps[3]));
                    lastPoint.set(Float.parseFloat(ps[2]), Float.parseFloat(ps[3]));
                }
                break;
                case 'q':
                case 'Q': {//二次贝塞尔曲线
                    String ps[] = findPoints(i);
                    lastPoint.set(Float.parseFloat(ps[2]), Float.parseFloat(ps[3]));
                    lPath.quadTo(Float.parseFloat(ps[0]), Float.parseFloat(ps[1]), Float.parseFloat(ps[2]), Float.parseFloat(ps[3]));
                }
                break;
                case 't':
                case 'T': {//T命令会跟在Q后面使用，用Q的结束点做起始点
                    String ps[] = findPoints(i);
                    lPath.quadTo(lastPoint.x,lastPoint.y, Float.parseFloat(ps[0]), Float.parseFloat(ps[1]));
                    lastPoint.set(Float.parseFloat(ps[0]), Float.parseFloat(ps[1]));
                }
                break;
                case 'a':
                case 'A':{//画弧
                }
                break;
                case 'z':
                case 'Z': {//结束
                    lPath.close();
                }
                break;
            }
        }
        return lPath;
    }

    private static String[] findPoints(int cmdIndexInPosition) {
        int cmdIndex = cmdPositions.get(cmdIndexInPosition);
        String pointString = svgPath.substring(cmdIndex + 1, cmdPositions.get(cmdIndexInPosition + 1));
        return pointString.split(",");
    }

    private static List<String> findCPoints(int cmdIndexInPosition){
        List<String> list=new ArrayList<>();
        int cmdIndex = cmdPositions.get(cmdIndexInPosition);
        String pointString = svgPath.substring(cmdIndex + 1, cmdPositions.get(cmdIndexInPosition + 1));
        String[] pointStr=pointString.split(" ");
        for (int i=0;i<pointStr.length;i++){
            if (!pointStr[i].equals("")){
                list.add(pointStr[i]);
            }
        }
        return list;
    }

    private static void findCommand() {
        cmdPositions.clear();
        while (mIndex < svgPathLenght) {
            char c = svgPath.charAt(mIndex);
            if ('A' <= c && c <= 'Z') {
                cmdPositions.add(mIndex);
            }else if ('a' <= c && c <= 'z') {
                cmdPositions.add(mIndex);
            }
            ++mIndex;
        }
    }
}
