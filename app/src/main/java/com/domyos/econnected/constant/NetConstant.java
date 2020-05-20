package com.domyos.econnected.constant;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;

/**
 * Created by HouWei on 16/8/17.
 */

public class NetConstant {
    /**
     * 网络请求成功
     */
    public static final int CODE_SUCCESS = 0;
    /**
     * token失效
     */
    public static final int CODE_TOKEN_INVALIDATE = -1000;

    /**
     * 未知网络错误
     */
    public static final String UNKNOW_NET_ERROR_MESSAGE = YDApplication.getInstance().getString(R.string.net_error_message);


}
