package com.tita.aixiaoyuan.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;

import com.tita.aixiaoyuan.model.ButterKnifeSerializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : Utils初始化相关
 * </pre>
 */
public class Utils {

    private static Context context;

    private Utils()  {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    public static void ButterKnifeBind(Activity activity){
        ButterKnifeSerializable bean = new ButterKnifeSerializable();
        Unbinder unbinder = ButterKnife.bind(activity);
        bean.setUnbinder(unbinder);
        activity.getIntent().putExtra("ButterKnifeSerializable", bean);
    }

    public static void ButterKnifeUnBind(Activity activity){
        ButterKnifeSerializable bean = (ButterKnifeSerializable) activity.getIntent().getSerializableExtra("ButterKnifeSerializable");
        bean.getUnbinder().unbind();
    }

    public static int getDevHeight(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}