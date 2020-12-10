package com.tita.aixiaoyuan.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.hjq.toast.ToastUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LifecycleApplication extends Application {
    private static final String TAG = "AppLifecycleCallback";
    public static Application		mApplication;


    private static LifecycleApplication INSTANCE;

    public static LifecycleApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(LifecycleApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(LifecycleApplication a) {
        LifecycleApplication.INSTANCE = a;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        ToastUtils.init(this);
        Utils.init(this);
        init();

        ///TODO 集成：1.8、初始化IM SDK，并注册消息接收器，只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            //BmobIM.init(this);
            //BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void init() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("Lifecycle",activity.getLocalClassName()+" was Created"+"activity==null   "
                        +(activity==null)+"     activity.isFinishing()  "+(activity.isFinishing())+"    activity.isDestroyed()  "+activity.isDestroyed());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("Lifecycle",activity.getLocalClassName()+" was Started"+"activity==null   "
                        +(activity==null)+"     activity.isFinishing()   "+(activity.isFinishing())+"   activity.isDestroyed()  "+activity.isDestroyed());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                StatusBarUtil.setFitsSystemWindow(activity,true);
                // 弱引用持有当前 Activity 实例
                ActivityStackManager.getInstance().setCurrentActivity(activity);
                // Activity 页面栈方式
                ActivityStackManager.getInstance().setTopActivity(activity);
                Log.e("Lifecycle",activity.getLocalClassName()+" was oResumed"+"activity==null   "
                        +(activity==null)+"activity.isFinishing()   "+(activity.isFinishing())+"activity.isDestroyed() "+activity.isDestroyed());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("Lifecycle",activity.getLocalClassName()+" was Pauseed"+"activity==null   "
                        +(activity==null)+"activity.isFinishing()   "+(activity.isFinishing())+"activity.isDestroyed()  "+activity.isDestroyed());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("Lifecycle",activity.getLocalClassName()+" was Stoped"+"activity==null    "
                        +(activity==null)+"activity.isFinishing()   "+(activity.isFinishing())+"activity.isDestroyed() "+activity.isDestroyed());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("Lifecycle",activity.getLocalClassName()+" was SaveInstanceState"+"activity==null "
                        +(activity==null)+"activity.isFinishing()   "+(activity.isFinishing())+"activity.isDestroyed()  "+activity.isDestroyed());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStackManager.getInstance().removeActivity(activity);
                Log.e("Lifecycle",activity.getLocalClassName()+" was Destroyed"+"activity==null"
                        +(activity==null)+"  activity.isFinishing()  "+(activity.isFinishing())+"  activity.isDestroyed()"+activity.isDestroyed());
            }
        });
    }
    private static void startLauncherActivity(Activity activity) {
        try {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
            String launcherClassName = launchIntent.getComponent().getClassName();
            String className = activity.getComponentName().getClassName();

            if (TextUtils.isEmpty(launcherClassName) || launcherClassName.equals(className)) {
                return;
            }

            Log.e(TAG, "launcher ClassName --> " + launcherClassName);
            Log.e(TAG, "current ClassName --> " + className);

            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(launchIntent);
            activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}