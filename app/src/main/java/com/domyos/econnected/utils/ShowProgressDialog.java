package com.domyos.econnected.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;

public class ShowProgressDialog {
    //展示对话框
    public static void showDialog(ProgressDialog progressDialog, String msg, Context context) {
        if (progressDialog == null) {
            //创建ProgressDialog对象
            progressDialog = new ProgressDialog(context);
            //设置进度条风格，风格为圆形，旋转的
            progressDialog.setProgressStyle(
                    ProgressDialog.STYLE_SPINNER);
            //设置ProgressDialog 标题图标
            progressDialog.setIcon(android.R.drawable.btn_star);
            //设置ProgressDialog 的进度条是否不明确
            progressDialog.setIndeterminate(false);
            //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.setCancelable(true);
        }
        //设置ProgressDialog 提示信息
        progressDialog.setMessage(msg);
        // 让ProgressDialog显示
        progressDialog.show();
    }


    public static void disProgressDialog(ProgressDialog progressDialog) {

        if (progressDialog.isShowing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
