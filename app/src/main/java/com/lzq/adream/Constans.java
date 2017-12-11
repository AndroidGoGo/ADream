package com.lzq.adream;

import android.os.Environment;

/**
 * Created by Administrator on 2017/3/27.
 */
public class Constans {

    public final static String HOME_URL = "http://119.23.134.242:8080/forms/FrmLogin";

    /**
     * 设备类型
     */
    public static final String DEVICE_TYPE = "android";

    /**
     *  shared 设置的key
     */
    public static String SHARED_SETTING_TAB = "setting";
    /**
     *  shared 设置的key
     */
    public static String SHARED_USER_TAB = "user";
    /**
     * 缩放程度  shared   key
     */
    public static String SCALE_SHAREDKEY = "InitialScale";
    /**
     * 第一次打开的页面  key
     */
    public static String HOME = "home";
    /**
     * 是不是第一次打开app key
     */
    public static String IS_FIRST_SHAREDKEY = "first";
    public static String FAIL_NUM_SHAREDKEY = "load";
    /**
     *  OcrDataPath路径
     */
    public static String OCR_PATH = "OcrDataPath";
    /**
     *
     */
    public static String SHARED_START_URL = "START";
    /**
     *  shared 设置的 消息页
     */
    public static String SHARED_MSG_URL = "MSG_URL";
    /**
     * //缓存文件列表
     */
    public final static String CONFIGNAME = "cahcefile.txt";

    String MD5_Sign="8a3019047f961e7ec90304f31352e049";

    //------------------------------------app的SD卡文件目录-----------------------------------
    public static final String DEFPATH = Environment.getExternalStorageDirectory()+ "/Treasure";
    public static final String IMAGEPATH = DEFPATH + "/image";
    public static final String AVATARPATH =DEFPATH+"/icon";
    public static final String VOICEPATH = DEFPATH + "/voice";
    public static final String MOIVEPATH = DEFPATH + "/moive";

    public final static String WX_appId = "wxc429effa4562439c";
    public final static String WX_Secret="6a59e0fe5f00e3dad6629af26b0fca32";
    /**
     * 文件存储跟目录路径
     */
    public final static String APP_PATH = "app";
    public final static String DATA_PATH = "data";
    public final static String CONFIG_PATH = "config";
    public final static String IMAGE_PATH = "image";
    public final static String TESSDATA_PATH = "tessdata";
  /*  public static String getAppPath(String Dir){
        File file = MyApplication.getInstance().getExternalFilesDir(Dir);
        if (!file.exists()) file.mkdirs();
        return file.getAbsolutePath();
    }*/

    /**
     * 接收网络变化 连接/断开 since 1.6.3
     */
    public final static String CONNECTION = "cn.jpush.android.intent.CONNECTION";
    /**
     * 用户打开自定义通知栏的intent
     */
    public final static String NOTIFICATION_OPENED = "cn.jpush.android.intent.NOTIFICATION_OPENED";
    /**
     * 用户接收SDK通知栏信息的intent-
     */
    public final static String NOTIFICATION_RECEIVED = "cn.jpush.android.intent.NOTIFICATION_RECEIVED";

    /**
     * 用户接收SDK消息的intent
     */
    public final static String MESSAGE_RECEIVED = "cn.jpush.android.intent.MESSAGE_RECEIVED";
    /**
     * 用户注册SDK的intent
     */
    public final static String REGISTRATION = "cn.jpush.android.intent.REGISTRATION";
}
