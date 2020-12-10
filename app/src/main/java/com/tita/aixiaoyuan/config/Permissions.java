package com.tita.aixiaoyuan.config;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {
    /**
     * TODO 1、兼容android6.0运行时权限
     * 检查权限
     *
     * @param requestCode
     */
    public static void checkStoragePermissions(int requestCode, Activity activity, Context context) {
        List<String> permissions = new ArrayList<>();
        int permissionCheckWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheckWrite != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        int permissionCheckRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissions.size() > 0) {
            String[] missions = new String[]{};
            ActivityCompat.requestPermissions(activity, permissions.toArray(missions), requestCode);
        } else {

        }
    }
}
