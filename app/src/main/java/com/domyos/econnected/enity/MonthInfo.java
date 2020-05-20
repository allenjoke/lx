package com.domyos.econnected.enity;

public class MonthInfo {
    private boolean exec;
    private String info;

    private MonthData[] data;
    private MonthData monthData;

    public boolean isExec() {
        return exec;
    }

    public void setExec(boolean exec) {
        this.exec = exec;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public MonthData[] getData() {
        return data;
    }

    public void setData(MonthData[] data) {
        this.data = data;
    }

    public MonthData getMonthData() {
        return monthData;
    }

    public void setMonthData(MonthData monthData) {
        this.monthData = monthData;
    }

    public class MonthData {
        public MonthData(int type, String timestamp, int max_speed, int avg_speed, int max_rise, int avg_rise, int calorie, int odometer, int elapse, int max_hr, int avg_hr, int max_rev, int avg_rev, int max_res, int avg_res, int max_watt, int avg_watt) {
            this.type = type;
            this.timestamp = timestamp;
            this.max_speed = max_speed;
            this.avg_speed = avg_speed;
            this.max_rise = max_rise;
            this.avg_rise = avg_rise;
            this.calorie = calorie;
            this.odometer = odometer;
            this.elapse = elapse;
            this.max_hr = max_hr;
            this.avg_hr = avg_hr;
            this.max_rev = max_rev;
            this.avg_rev = avg_rev;
            this.max_res = max_res;
            this.avg_res = avg_res;
            this.max_watt = max_watt;
            this.avg_watt = avg_watt;
        }

        private  int type;//健身机器类型（int类型）
        private   String timestamp;//时间戳（string）
        private  int max_speed;//最高速度（int）
        private  int avg_speed;//平均速度（int）
        private  int max_rise;//最大扬升（int）
        private  int avg_rise;//平均扬升
        private  int calorie;//卡路里（int）
        private  int odometer;//总里程（int）
        private  int elapse;//总时间（int）
        private  int  max_hr;//最大心率（int）
        private  int avg_hr;//平均心率（int）
        private  int max_rev;//最大转速（int）
        private  int avg_rev;//平均转速（int）
        private  int max_res;//最大阻力（int）
        private  int avg_res;//平均阻力（int）
        private  int max_watt;//最大瓦特（int）
        private  int avg_watt;//平均瓦特（int）

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public int getMax_speed() {
            return max_speed;
        }

        public void setMax_speed(int max_speed) {
            this.max_speed = max_speed;
        }

        public int getAvg_speed() {
            return avg_speed;
        }

        public void setAvg_speed(int avg_speed) {
            this.avg_speed = avg_speed;
        }

        public int getMax_rise() {
            return max_rise;
        }

        public void setMax_rise(int max_rise) {
            this.max_rise = max_rise;
        }

        public int getAvg_rise() {
            return avg_rise;
        }

        public void setAvg_rise(int avg_rise) {
            this.avg_rise = avg_rise;
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

        public int getElapse() {
            return elapse;
        }

        public void setElapse(int elapse) {
            this.elapse = elapse;
        }

        public int getMax_hr() {
            return max_hr;
        }

        public void setMax_hr(int max_hr) {
            this.max_hr = max_hr;
        }

        public int getAvg_hr() {
            return avg_hr;
        }

        public void setAvg_hr(int avg_hr) {
            this.avg_hr = avg_hr;
        }

        public int getMax_rev() {
            return max_rev;
        }

        public void setMax_rev(int max_rev) {
            this.max_rev = max_rev;
        }

        public int getAvg_rev() {
            return avg_rev;
        }

        public void setAvg_rev(int avg_rev) {
            this.avg_rev = avg_rev;
        }

        public int getMax_res() {
            return max_res;
        }

        public void setMax_res(int max_res) {
            this.max_res = max_res;
        }

        public int getAvg_res() {
            return avg_res;
        }

        public void setAvg_res(int avg_res) {
            this.avg_res = avg_res;
        }

        public int getMax_watt() {
            return max_watt;
        }

        public void setMax_watt(int max_watt) {
            this.max_watt = max_watt;
        }

        public int getAvg_watt() {
            return avg_watt;
        }

        public void setAvg_watt(int avg_watt) {
            this.avg_watt = avg_watt;
        }

        @Override
        public String toString() {
            return "MonthData{" +
                    "type=" + type +
                    ", timestamp='" + timestamp + '\'' +
                    ", max_speed=" + max_speed +
                    ", avg_speed=" + avg_speed +
                    ", max_rise=" + max_rise +
                    ", avg_rise=" + avg_rise +
                    ", calorie=" + calorie +
                    ", odometer=" + odometer +
                    ", elapse=" + elapse +
                    ", max_hr=" + max_hr +
                    ", avg_hr=" + avg_hr +
                    ", max_rev=" + max_rev +
                    ", avg_rev=" + avg_rev +
                    ", max_res=" + max_res +
                    ", avg_res=" + avg_res +
                    ", max_watt=" + max_watt +
                    ", avg_watt=" + avg_watt +
                    '}';
        }
    }


}
