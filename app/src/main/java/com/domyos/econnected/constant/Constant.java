package com.domyos.econnected.constant;

import android.text.TextUtils;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HouWei on 16/8/24.
 */

public class Constant {

    static public class MapConfiguration {
        public static int DEFAULT_ZOOM_LEVEL = 13;
        //AIzaSyA7VNrcdWs8SKCd9vBX8as3m94WDP1gyCQ
        public static String GOOGLE_MAP_API_KEY = "AIzaSyBL8Ms5lhs6c6vI14KSonmj1Hdoz9Nr8tg";
    }
    static public class UrlPath {
        public static String GOOGLE_MAP_API_URL = "https://maps.googleapis.com";
        public static String GOOGLE_ROADS_API_URL = "https://roads.googleapis.com";

    }


    public interface Config {
//        String USB_STORAGE_PATH = "/sdcard/ttt/";
//                String USB_STORAGE_PATH = "/storage/sdcard0/";
        /**
         * 设备外置存储设备路径
         */
        String USB_STORAGE_PATH = "/mnt/usb_storage/";
//        String QI_NIU_URL_PREFIX = "http://ocpfm9xcx.bkt.clouddn.com/";

        /**
         * 外置存储设备中升级文件的名称
         */
        String USB_STORAGE_UPGRADE_FILE_NAME = "treadmill_upgrade.apk";

        /**
         * 外置存储设备中升级文件的存放路径
         */
        String USB_STORAGE_UPGRADE_FILE_PATH_PREFIX = "/mnt/media_rw/";

        /**
         * 七牛云存储前缀
         */
        String QI_NIU_URL_PREFIX = "http://resources.yuedongtech.com//";
        /**
         * 升级包存放目标
         */
        String DOWNLOAD_APK_FOLDER_PATH = YDApplication.getInstance().getFilesDir() + File.separator + "Treadmill" + File.separator;
        /**
         * 升级包文件下载路径
         */
        String DOWNLOAD_APK_PATH = DOWNLOAD_APK_FOLDER_PATH + "download.apk";
        /**
         * 管理员密码
         */
        String ADMIN_PWD = "34e78d94d0610fd5ac76dcbf6b37f866";

        /**
         * 系统设置密码
         */
        String SYS_SETTING_PWD = "16f7925ee4b62edd67100e1e1d861f46";

        /**
         * 检测ota升级的url前缀
         */
        String OTA_URL_PREFIX = "http://123.56.199.233:2300/OtaUpdater/android";

        /**
         * OTA包存放路径
         */

//        String OTA_FILE_PARENT_FOLDER_PATH = "/data/media/0/";
        //        String OTA_FILE_PARENT_FOLDER_PATH = "/cache/";

        String OTA_FILE_PARENT_FOLDER_PATH = "/storage/emulated/0/";
        String OTA_FILE_PATH = OTA_FILE_PARENT_FOLDER_PATH + "update.zip";

        /**
         * 恢复出厂设置intent
         */
        String INTENT_CLEAR_MASTER = "com.yuedong.treadmill.MASTER_CLEAR";
        /**
         * 重启intent
         */
        String INTENT_REBOOT = "com.yuedong.treadmill.REBOOT";
        /**
         * 重启intent
         */
        String INTENT_BACKRUNNING = "com.yuedong.treadmill.BACKRUNNING";
        /**
         * 重启intent
         */
        String INTENT_LOGOPT = "com.yuedong.treadmill.LOGOPT";
        /**
         * 运动自动停止时间显示（超过7*24小时，运动自动停止)
         */
        long RUNNING_TIME_MINUTE_LIMIT = 7 * 24 * 60;

    }

    public static class VesrionList {
        /**
         * 不包含重启功能的版本号
         */
        private static final List<String> versionWithReboot = new ArrayList<>();

        static {
            if (versionWithReboot != null && versionWithReboot.size() > 0) {
                versionWithReboot.clear();
            }

            versionWithReboot.add("TD_P2_LVDS1024_KD101_DFL101_NB197SM_20161203_V010_A107");
            versionWithReboot.add("TD_P1_EDP1366_BOE156_DFL156_ILI2302_20161210_V011_A112");
            versionWithReboot.add("TD_P1_EDP1366_BOE156_DFL156_ILI2302_20161215_V015");
        }

        public static List<String> getVersionWithReboot() {
            return versionWithReboot;
        }
    }



    public static class Avatar {

        private static final HashMap<Integer, String> localUserAvatarResIdToNameMap = new HashMap<>();
        private static final HashMap<String, Integer> localUserAvatarNameToResIdMap = new HashMap<>();
        private static final List<Integer> localUserAvatartResIdList = new ArrayList<>();

        public static List<Integer> getLocalUserAvatartResIdList() {
            return localUserAvatartResIdList;
        }

        public static int   getAvatarResIdByName(String name) {
            Integer result = localUserAvatarNameToResIdMap.get(name);
            if (result != null) {
                return result;
            }
            return 0;
        }

        public static String getAvatarNameByResId(int resId) {
            String result = localUserAvatarResIdToNameMap.get(resId);
            if (TextUtils.isEmpty(result)) {
                return "";
            }
            return result;
        }


        static {
            localUserAvatarResIdToNameMap.put(R.mipmap.shu, "head_01");
            localUserAvatarNameToResIdMap.put("head_01", R.mipmap.shu);
            localUserAvatartResIdList.add(R.mipmap.shu);

            localUserAvatarResIdToNameMap.put(R.mipmap.niu, "head_02");
            localUserAvatarNameToResIdMap.put("head_02", R.mipmap.niu);
            localUserAvatartResIdList.add(R.mipmap.niu);

            localUserAvatarResIdToNameMap.put(R.mipmap.hu, "head_03");
            localUserAvatarNameToResIdMap.put("head_03", R.mipmap.hu);
            localUserAvatartResIdList.add(R.mipmap.hu);

            localUserAvatarResIdToNameMap.put(R.mipmap.tu, "head_04");
            localUserAvatarNameToResIdMap.put("head_04", R.mipmap.tu);
            localUserAvatartResIdList.add(R.mipmap.tu);

            localUserAvatarResIdToNameMap.put(R.mipmap._long, "head_05");
            localUserAvatarNameToResIdMap.put("head_05", R.mipmap._long);
            localUserAvatartResIdList.add(R.mipmap._long);

            localUserAvatarResIdToNameMap.put(R.mipmap.she, "head_06");
            localUserAvatarNameToResIdMap.put("head_06", R.mipmap.she);
            localUserAvatartResIdList.add(R.mipmap.she);

            localUserAvatarResIdToNameMap.put(R.mipmap.ma, "head_07");
            localUserAvatarNameToResIdMap.put("head_07", R.mipmap.ma);
            localUserAvatartResIdList.add(R.mipmap.ma);

            localUserAvatarResIdToNameMap.put(R.mipmap.yang, "head_08");
            localUserAvatarNameToResIdMap.put("head_08", R.mipmap.yang);
            localUserAvatartResIdList.add(R.mipmap.yang);

            localUserAvatarResIdToNameMap.put(R.mipmap.hou, "head_09");
            localUserAvatarNameToResIdMap.put("head_09", R.mipmap.hou);
            localUserAvatartResIdList.add(R.mipmap.hou);

            localUserAvatarResIdToNameMap.put(R.mipmap.ji, "head_10");
            localUserAvatarNameToResIdMap.put("head_10", R.mipmap.ji);
            localUserAvatartResIdList.add(R.mipmap.ji);

            localUserAvatarResIdToNameMap.put(R.mipmap.gou, "head_11");
            localUserAvatarNameToResIdMap.put("head_11", R.mipmap.gou);
            localUserAvatartResIdList.add(R.mipmap.gou);

            localUserAvatarResIdToNameMap.put(R.mipmap.zhu, "head_12");
            localUserAvatarNameToResIdMap.put("head_12", R.mipmap.zhu);
            localUserAvatartResIdList.add(R.mipmap.zhu);
        }

    }

    public static final String CUSTOMER_ID = "cutomerID";
    public static final String AD_ADDRESS = "AD address";
    public static final String AD_DATE = "AD DATE";
    public static final String AD_SIZE = "AD SIZE";
    public static final String XUNFEI_APPID = "=5db03b6a";
}
