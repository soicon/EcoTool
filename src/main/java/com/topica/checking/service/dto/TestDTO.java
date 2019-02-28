package com.topica.checking.service.dto;

import java.io.Serializable;

public class TestDTO implements Serializable {
    private String url;
    private int result;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
