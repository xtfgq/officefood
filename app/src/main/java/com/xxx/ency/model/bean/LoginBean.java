package com.xxx.ency.model.bean;

public class LoginBean {
    /**
     * 登录token
     */
    String token;
    /**
     * 登录角色
     */
    int model;
    /**
     * 登录用户名
     */
    String username;
    String userid;
    String portrait;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
