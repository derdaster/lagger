package com.android.lagger.responseObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class ResponseObject {
    private String response;
    private Boolean isError;

    public ResponseObject() {
    }

    public ResponseObject(String response, Boolean isError) {
        this.response = response;
        this.isError = isError;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public Boolean isError() {
        return isError;
    }
}
