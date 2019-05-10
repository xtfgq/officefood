package com.xxx.ency.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * SharePreferences管理类
 * Created by xiarh on 2017/9/21.
 */

public class SharePrefManager {

    private static final String SHAREDPREFERENCES_NAME = "my_sp";

    private SharedPreferences SPfres;

    @Inject
    public SharePrefManager(Context context) {
        SPfres = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 设置省流量模式
     */
    public void setProvincialTrafficPatterns(boolean event) {
        SPfres.edit().putBoolean("provincial_traffic_patterns", event).commit();
    }

    public boolean getProvincialTrafficPattern() {
        return SPfres.getBoolean("provincial_traffic_patterns", false);
    }

    public void setLocalProvincialTrafficPatterns(boolean event) {
        SPfres.edit().putBoolean("local_provincial_traffic_patterns", event).commit();
    }

    public boolean getLocalProvincialTrafficPatterns() {
        return SPfres.getBoolean("local_provincial_traffic_patterns", false);
    }

    /**
     * 设置夜间模式
     *
     * @param event
     */
    public void setNightMode(boolean event) {
        SPfres.edit().putBoolean("nightmode", event).commit();
    }

    public boolean getNightMode() {
        return SPfres.getBoolean("nightmode", false);
    }

    public void setLocalMode(int localMode) {
        SPfres.edit().putInt("localMode", localMode).commit();
    }

    public int getLocalMode() {
        return SPfres.getInt("localMode", -1);
    }

    /**
     * @param event 0,保管员
     */
    public void setLoginModel(int event) {
        SPfres.edit().putInt("LoginModel", event).commit();
    }

    /**
     * 设置token
     *
     * @param token
     */
    public void setLoginToken(String token) {
        SPfres.edit().putString("LoginToken", token).commit();
    }

    public void setDeviceId(String deviceId) {
        SPfres.edit().putString("deviceId", deviceId).commit();
    }

    /**
     * 得到token
     *
     * @return
     */
    public String getLocalToken() {
        return SPfres.getString("LoginToken", "");
    }

    /**
     * 设置用户
     *
     * @param user
     */
    public void setUser(String user) {
        SPfres.edit().putString("Name", user).commit();
    }
    public void setUseId(String userid) {
        SPfres.edit().putString("Userid", userid).commit();
    }

    public String getDeviceId() {
        return SPfres.getString("deviceId", "");
    }

    public String getUserName() {
        return SPfres.getString("Name", "");
    }
    public String getUserId() {
        return SPfres.getString("Userid", "");
    }
    public void clearData(){
        setUseId("");
        setLoginToken("");
        setUser("");
        setLocalMode(-1);
    }
    public void setLocation(String city) {
        SPfres.edit().putString("City", city).commit();
    }
    public String getLocation() {
        return SPfres.getString("City", "焦作市");
    }

}
