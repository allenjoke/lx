package com.domyos.econnected.heartRate;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author : SimonWang
 * date : 2019-11-20 15:51
 * description :
 */
public class HeartRateManager {
    private List<Integer> heartRateLists = new ArrayList<>();

    private HeartRateManager() {
    }

    private static final HeartRateManager manager = new HeartRateManager();

    public static HeartRateManager getInstance() {
        return manager;
    }


    public List<Integer> getHeartRateLists() {
        return heartRateLists;
    }

    public void addHeartRate(Integer heartRate) {
        this.heartRateLists.add(heartRate);
    }

    public void removeAllData() {
        this.heartRateLists = new ArrayList<>();
    }


    public int getMaxHeartRate() {
        int maxHeartRate = 0;
        maxHeartRate = Collections.max(heartRateLists);
        Log.d("simon", "maxHeartRate=" + maxHeartRate);
        return maxHeartRate;

    }

    public int getAvgHeartRate() {
        int avgHeartRate = 0;
        for (int i = 0; i < heartRateLists.size(); i++) {
            avgHeartRate += heartRateLists.get(i);
        }
        avgHeartRate = avgHeartRate / heartRateLists.size();
        return avgHeartRate;
    }

    /**
     * 50-80 热身，80-120 燃脂， 120-150 有氧， 150-180 无氧 ，180以上 极限
     *
     * @return 热身时间（秒）
     */
    public int getWarmUp() {
        int seconds = 0;
        for (int i = 0; i < heartRateLists.size(); i++) {
            if (heartRateLists.get(i) > 60 && heartRateLists.get(i) <= 90) {
                seconds++;
            }
        }
        return seconds;
    }

    /**
     * 50-80 热身，80-120 燃脂， 120-150 有氧， 150-180 无氧 ，180以上 极限
     *
     * @return 燃脂时间（秒）
     */
    public int getCalorieBurn() {
        int seconds = 0;
        for (int i = 0; i < heartRateLists.size(); i++) {
            if (heartRateLists.get(i) > 90 && heartRateLists.get(i) <= 120) {
                seconds++;
            }
        }
        return seconds;
    }

    /**
     * 50-80 热身，80-120 燃脂， 120-150 有氧， 150-180 无氧 ，180以上 极限
     *
     * @return 有氧耐力时间（秒）
     */
    public int getAerobic() {
        int seconds = 0;
        for (int i = 0; i < heartRateLists.size(); i++) {
            if (heartRateLists.get(i) > 120 && heartRateLists.get(i) <= 150) {
                seconds++;
            }
        }
        return seconds;

    }

    /**
     * 50-80 热身，80-120 燃脂， 120-150 有氧， 150-180 无氧 ，180以上 极限
     *
     * @return 无氧耐力时间（秒）
     */
    public int getAnaerobic() {
        int seconds = 0;
        for (int i = 0; i < heartRateLists.size(); i++) {
            if (heartRateLists.get(i) > 150 && heartRateLists.get(i) <= 180) {
                seconds++;
            }
        }
        return seconds;
    }

    /**
     * 50-80 热身，80-120 燃脂， 120-150 有氧， 150-180 无氧 ，180以上 极限
     *
     * @return 极限时间（秒）
     */
    public int getLimit() {
        int seconds = 0;
        for (int i = 0; i < heartRateLists.size(); i++) {
            if (heartRateLists.get(i) > 180) {
                seconds++;
            }
        }
        return seconds;
    }

}
