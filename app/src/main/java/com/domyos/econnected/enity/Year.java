package com.domyos.econnected.enity;

public class Year {
    private int elapse;
    private int calorie;
    private int odometer;
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
        return "Year{" +
                "elapse=" + elapse +
                ", calorie=" + calorie +
                ", odometer=" + odometer +
                ", avg_hr=" + avg_hr +
                '}';
    }
}
