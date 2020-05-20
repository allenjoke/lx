package com.domyos.econnected.net.request;

/**
 * Created by HouWei on 16/8/17.
 */
public class ResponseEntity<T> {
    private int code;
    private String text;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
