package com.tita.aixiaoyuan.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tita.aixiaoyuan.model.ButterKnifeSerializable;

import java.io.File;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 保存照片路径工具
     */
    public static String IMG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "onlineStoreImg";

    public static String getImgPath() {
        File file = new File(IMG_PATH);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                Log.e("TAG", "文件夹创建失败");
                return Environment.DIRECTORY_PICTURES+"/"+"onlineStoreImg";
            } else {
                Log.e("TAG", "文件夹创建成功");
            }
        }else {

        }
        return IMG_PATH;
    }
    /**
     * 根据宽度自动调整(参照图片本身尺寸)高度<br/>
     * 需要开启 android:adjustViewBounds="true"
     * @param context
     * @param view
     * @param imageViewWidth    指定的宽度，<1时则取屏幕宽度
     */
    public static void autoSizeImageViewHeight(Context context, ImageView view,
                                               int imageViewWidth) {
        if (context == null || view == null) {
            return;
        }
        if (imageViewWidth < 1) {
            imageViewWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = imageViewWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(lp);
        view.setMaxWidth(imageViewWidth);
        view.setMaxHeight(imageViewWidth * 5);
    }
    // 判断网络是否连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断电话号码是否有效
     *
     * @param phoneNumber
     * @return true 有效 / false 无效
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {

        boolean isValid = false;

        String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断邮箱地址是否有效
     *
     * @param email
     * @return true 有效 / false 无效
     */
    public static boolean isEmailValid(String email)
    {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }
    /**
     * 生成订单ID = 时间 + 6位随机数
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;//6位随机数

        return System.currentTimeMillis() + String.valueOf(number);
    }

    /**
     * 生成商品ID = 时间 + 6位随机数
     */
    public static synchronized String genUniqueKeyProduct() {
        Random random = new Random();
        Integer number = random.nextInt(90) + 10;//2位随机数

        return System.currentTimeMillis() + String.valueOf(number);
    }
    /**
     * 得到资源文件中图片的Uri
     * @param context 上下文对象
     * @param id 资源id
     * @return Uri
     */
    public static Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }
}