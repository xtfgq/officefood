package com.xxx.ency.config;

import java.io.File;

/**
 * 常量
 * Created by xiarh on 2017/9/21.
 */

public class Constants {

    public static final String ONE_URL = "https://meiriyiwen.com/";


    // bugly APP ID
    public static final String BUGLY_APP_ID = "792af661e6";
    public static String XINGE = "";


    // fir.im API Token
    public static final String FIR_IM_API_TOKEN = "f9bc86fa38d9a29d60574d0e0035be31";

    // fir.im ID
    public static final String FIR_IM_ID = "5c84ce6eca87a82b51e4594c";

    // 开眼视频pdid
    public static final String EYEPETIZER_UDID = "79a95dc6b649489383e976b5b97d129f6d592fad";

    public static final String PATH_DATA = EncyApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";
    public static  String HOST = "http://api.fc1b.com/";
//    public static final String HOST = "http://zhengzhouapi.fc1b.com/";
//   public static final String HOST = "http://192.168.4.143:8080/";

    public static final int NET_CODE_SUCCESS = 0;
    public static final int NET_CODE_LOGIN = 1001;
    public static final int TYPE_DEFAULT = 0;

    public static final int TYPE_WEIXIN = 1;

    public static final int TYPE_GANK = 2;

    public static final int TYPE_VIDEO = 3;
    public static String token = "";
}