package com.domyos.econnected.net;

import android.util.Log;

import com.domyos.econnected.constant.NetConstant;
import com.domyos.econnected.net.request.ResponseEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by HouWei on 16/8/18.
 */

public abstract class ResponseCallBack<T> implements Callback<T> {


    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try{
            Log.e("onResponse","entity ==== errcode: error="+response.code());
        }catch(Exception e){

        }

        if (response.isSuccessful()) {
            if (response.body() instanceof ResponseEntity) {
                ResponseEntity entity = (ResponseEntity) response.body();
                Log.e("onResponse","entity errcode:"+entity.getCode());
                if (entity.getCode() == NetConstant.CODE_SUCCESS){
                    onSuccess(response.body());
                }/*else if(entity.getCode() == NetConstant.CODE_TOKEN_INVALIDATE){
                    // token 失效 清空本地缓存 回到未登录状态;
                    onError(entity.getText());
                }*/else{
                    onError(entity.getText());
                }
            } else {
                onSuccess(response.body());
            }
        } else {
            String errorMessage = NetConstant.UNKNOW_NET_ERROR_MESSAGE;
            onError(errorMessage);
        }
    }

    protected abstract void onSuccess(T body);


    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        String errorMessage = NetConstant.UNKNOW_NET_ERROR_MESSAGE;
        onError(errorMessage);
    }

    protected abstract void onError(String errorMessage);

}
