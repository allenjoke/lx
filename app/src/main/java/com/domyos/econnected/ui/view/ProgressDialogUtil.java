package com.domyos.econnected.ui.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.domyos.econnected.R;
import com.domyos.econnected.ui.BaseActivity;


/**
 * Created by HouWei on 15/12/2.
 */
public class ProgressDialogUtil {
    public static ProgressDialog mProgressDialog = null;


    public static void showProgressDialog(Context context) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(context.getString(R.string.waiting) + "...");
            mProgressDialog.setTitle(context.getString(R.string.waiting));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(true);
        } else {
            dismissProgressDialog();
            if (context != null) {
                mProgressDialog = ProgressDialog.show(context, context.getString(R.string.waiting), context.getString(R.string.waiting) + "...", true, true);
                mProgressDialog.setCanceledOnTouchOutside(false);
            }
        }
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).setmProgressDialog(mProgressDialog);
        }
    }

    public static void showProgressDialog(Context context, String title, String message, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.setTitle(title);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setOnCancelListener(cancelListener);
        } else {
            dismissProgressDialog();
            if (context != null) {
                mProgressDialog = ProgressDialog.show(context, title, message, true, cancelable, cancelListener);
                mProgressDialog.setCanceledOnTouchOutside(false);
            }
        }
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).setmProgressDialog(mProgressDialog);
        }
    }


    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        dismissProgressDialog();
    }


}
