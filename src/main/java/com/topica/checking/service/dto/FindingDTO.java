package com.topica.checking.service.dto;

public class FindingDTO {
    private String url;
    private String fileName;
    private String verInfo;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVerInfo() {
        return verInfo;
    }

    public void setVerInfo(String verInfo) {
        this.verInfo = verInfo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
