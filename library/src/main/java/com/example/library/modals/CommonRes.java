package com.example.library.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonRes {

    @SerializedName("StatusCode")
    @Expose
    private int statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ResponseData")
    @Expose
    private Object responseData;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public CommonRes withStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommonRes withMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    public CommonRes withResponseData(Object responseData) {
        this.responseData = responseData;
        return this;
    }

}
