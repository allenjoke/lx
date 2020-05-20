package com.domyos.econnected.enity;

public class Week {
    //总时间
    private int elapse;
    //总卡路里
    private int calorie;
    //总里程
    private int odometer;
    //总平均心率
    private int avg_hr;

    public int getElapse() {
        return elapse;
    }

    public void setElapse(int elapse) {
        this.elapse = elapse;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public int getAvg_hr() {
        return avg_hr;
    }

    public void setAvg_hr(int avg_hr) {
        this.avg_hr = avg_hr;
    }

    @Override
    public String toString() {
        return "Week{" +
                "elapse=" + elapse +
                ", calorie=" + calorie +
                ", odometer=" + odometer +
                ", avg_hr=" + avg_hr +
                '}';
    }
}
