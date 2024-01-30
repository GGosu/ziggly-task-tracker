package com.ziggly.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class UserSessionDTO {
    private Integer id;
    private String userAgent;
    private String app;
    private String os;
    private String ip;
    private Long loginFirst;
    private Long loginLast;

    //getter and setter:

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getLoginFirst() {
        return loginFirst;
    }

    public void setLoginFirst(Long loginFirst) {
        this.loginFirst = loginFirst;
    }

    public Long getLoginLast() {
        return loginLast;
    }

    public void setLoginLast(Long loginLast) {
        this.loginLast = loginLast;
    }
}
