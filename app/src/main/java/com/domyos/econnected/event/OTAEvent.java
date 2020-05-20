package com.domyos.econnected.event;

import com.domyos.econnected.enity.OTAInfo;

/**
 * Created by HouWei on 2016/12/15.
 */

public class OTAEvent {
    public int action;
    public OTAInfo otaInfo;


    public OTAEvent(int action) {
        this.action = action;
    }

    public OTAEvent(int action, OTAInfo otaInfo) {
        this.action = action;
        this.otaInfo = otaInfo;
    }

    /**
     * 下载状态变化
     */
    public static final int ACTION_OTA_DONWLOADING =  50;
    /**
     * 未找到ota
     */
    public static final int ACTION_OTA_NOT_FOUND = 51;
    /**
     * 下载完成
     */
    public static final int ACTION_OTA_DOWNLOAD_FINISH = 52;
    //测试临时用
    public static final int ACTION_OTA_01 =  53;

    public static final int ACTION_OTA_02 = 54;

    public static final int ACTION_OTA_03 = 55;
}
