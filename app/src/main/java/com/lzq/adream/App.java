package com.lzq.adream;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.lzq.adream.comm.DemoHelper;
import com.lzq.adream.dataservice.db.greendao.DaoMaster;
import com.lzq.adream.dataservice.db.greendao.DaoSession;
import com.lzq.adream.model.LoginUser;
import com.lzq.adream.utils.SharedPreferencesUtils;

import java.util.Iterator;
import java.util.List;


public class App extends MultiDexApplication {
    public static App sApp;
    public static long exitTime = 0;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private EMOptions mOptions;
    public static String sUserName;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        DemoHelper.getInstance().init(sApp);
       // saveDataBase();
        mOptions = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        mOptions.setAcceptInvitationAlways(false);
        mOptions.setAutoLogin(true);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e("初始化环信SDK", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EaseUI.getInstance().init(getApplicationContext(), mOptions);

        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().setDebugMode(true);
    }

    private void saveDataBase() {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "userinfo.db", null);
        mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());

    }

    public static DaoSession getDaoSession() {
        DaoSession daoSession = mDaoMaster.newSession();

        return daoSession;
    }


    public static Context getAppContext() {
        return sApp;
    }

    public static Resources getAppResources() {
        return sApp.getResources();
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public LoginUser getLoginUser() {
        String str = SharedPreferencesUtils.getString(this, SPContans.LAST_LOGIN_USER_KEY);
        try {
            if (!TextUtils.isEmpty(str)) {
                return JSON.parseObject(str, LoginUser.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
