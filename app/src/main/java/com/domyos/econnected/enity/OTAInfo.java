package com.domyos.econnected.enity;

import java.io.Serializable;

/**
 * Created by HouWei on 2016/12/15.
 */

public class OTAInfo implements Serializable{
    /**
     * 下载链接
     */
    private String url;
    /**
     * 版本号
     */
    private String version;
    /**
     * mid
     */
    private String sn;
    /**
     * 状态
     */
    private int state;
    /**
     * 下载进度
     */
    private float progressPercent;
    /**
     * 本地路径
     */
    private String localPath;


    public OTAInfo() {
    }

    public OTAInfo(String url, String version, String sn, int state, float progressPercent, String localPath) {
        this.url = url;
        this.version = version;
        this.sn = sn;
        this.state = state;
        this.progressPercent = progressPercent;
        this.localPath = localPath;
    }


    public String getUrl() {
        return url;
    }

    public OTAInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public OTAInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public OTAInfo setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public int getState() {
        return state;
    }

    public OTAInfo setState(int state) {
        this.state = state;
        return this;
    }

    public float getProgressPercent() {
        return progressPercent;
    }

    public OTAInfo setProgressPercent(float progressPercent) {
        this.progressPercent = progressPercent;
        return this;
    }

    public String getLocalPath() {
        return localPath;
    }

    public OTAInfo setLocalPath(String localPath) {
        this.localPath = localPath;
        return this;
    }

    /**
     * 准备就绪
     */
    public static final int STATE_PREPARED = 0;
    /**
     * 暂停中
     */
    public static final int STATE_PAUSED = 1;
    /**
     * 下载出错 - 页面提示无更新
     */
    public static final int STATE_ERROR= 2;
    /**
     * 下载中
     */
    public static final int STATE_PROGRESS = 3;
    /**
     * 下载完成
     */
    public static final int STATE_FINISHED = 4;
}
