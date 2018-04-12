package com.zhph.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by zhouliang on 2017/12/26.
 */
@Configuration
@ConfigurationProperties(prefix = "url",ignoreInvalidFields = false)
@PropertySource("classpath:interfaceDic.properties")
@Component
public class UrlConfig {
    private String fileUpload;
    private String fileDownload;
    private String badInformationUrl;
    private String loginCode;
    private String loginPw;
    private String meal;

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getBadInformationUrl() {
        return badInformationUrl;
    }

    public void setBadInformationUrl(String badInformationUrl) {
        this.badInformationUrl = badInformationUrl;
    }


    public String getLoginPw() {
        return loginPw;
    }

    public void setLoginPw(String loginPw) {
        this.loginPw = loginPw;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(String fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getFileDownload() {
        return fileDownload;
    }
    public void setFileDownload(String fileDownload) {
        this.fileDownload = fileDownload;
    }
}
