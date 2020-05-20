package com.domyos.econnected.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Created by HouWei on 16/8/13.
 */

public class ToastUtil {
    private static View view;

    public static void toast(Context context, String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int stringId) {
        Toast.makeText(context.getApplicationContext(), stringId, Toast.LENGTH_SHORT).show();
    }
}
