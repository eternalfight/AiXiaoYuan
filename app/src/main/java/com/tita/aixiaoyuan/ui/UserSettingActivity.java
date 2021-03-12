package com.tita.aixiaoyuan.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hjq.toast.ToastUtils;
import com.jude.utils.JUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.User;
import com.tita.aixiaoyuan.utils.GlideEngine;
import com.tita.aixiaoyuan.utils.Utils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserSettingActivity extends AppCompatActivity {

    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.sex_tv)
    TextView sex_tv;
    @BindView(R.id.phone)
    TextView phone_tv;
    @BindView(R.id.addr_tv)
    TextView addr_tv;
    @BindView(R.id.headImage)
    ImageView headImage;
    private String addr;
    private String phone;
    private String name;
    private String sex;
    private String[] sexArry = new String[]{"男", "女"};// 性别选择
    private String mediadata;
    //权限定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMER"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        sex = (String) BmobUser.getObjectByKey("sex");
        sex_tv.setText(sex);
        name = (String) BmobUser.getObjectByKey("nickname");
        name_tv.setText(name);
        phone = (String) BmobUser.getObjectByKey("mobilePhoneNumber");
        phone_tv.setText(phone);
        addr = (String) BmobUser.getObjectByKey("address");
        addr_tv.setText(addr);
        showHeadImg();

    }

    @OnClick({R.id.headImg,R.id.name,R.id.sex,R.id.phone_tv,R.id.locate,R.id.changePassword,R.id.outLogin,R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.headImg:
                selectHeadImg();
                break;
            case R.id.name:
                setNickName();
                break;
            case R.id.sex:
                showSexChooseDialog();
                break;
            case R.id.phone_tv:
                setPhoneNumber();
                break;
            case R.id.locate:
                setLocation();
                break;
            case R.id.changePassword:
                doChangePassword();
                break;
            case R.id.outLogin:
                doOutLogin();
                finish();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
    //动态申请权限
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void selectHeadImg() {
        verifyStoragePermissions(this);

        PictureSelector.create(this)
                .openGallery(PictureConfig.TYPE_IMAGE)
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(R.style.picture_WeChat_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofCustomWindowAnimationStyle(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out))// 自定义相册启动退出动画
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .imageSpanCount(4)// 每行显示个数 int
                .maxSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isSingleDirectReturn(true)//PictureConfig.SINGLE模式下是否直接返回
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG_Q)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isAndroidQTransform(true)
                .setOutputCameraPath(Utils.getImgPath())// 自定义拍照保存路径,可不填
                .withAspectRatio(1, 1)
                .isEnableCrop(true)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
                .isAndroidQTransform(false)
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .compressSavePath(Utils.getImgPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .cutOutQuality(90)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        Log.i("uploadpath", "是否压缩:" + media.isCompressed());
                        Log.i("uploadpath", "压缩:" + media.getCompressPath());
                        Log.i("uploadpath", "原图:" + media.getPath());
                        Log.i("uploadpath", "绝对路径:" + media.getRealPath());
                        Log.i("uploadpath", "是否裁剪:" + media.isCut());
                        Log.i("uploadpath", "裁剪:" + media.getCutPath());
                        Log.i("uploadpath", "是否开启原图:" + media.isOriginal());
                        Log.i("uploadpath", "原图路径:" + media.getOriginalPath());
                        Log.i("uploadpath", "Android Q 特有Path:" + media.getAndroidQToPath());
                        Log.i("uploadpath", "宽高: " + media.getWidth() + "x" + media.getHeight());
                        Log.i("uploadpath", "Size: " + media.getSize());
                            if (media.getCutPath() != null){
                                mediadata = media.getCutPath();
                            }

                        // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
                    }
                    if (selectList.size() != 0 ){
                        headImage.setImageURI(Uri.parse(selectList.get(0).getCompressPath()));
                    }
                    upload(mediadata);
                    Log.i("uploadpath", "onActivityResult: "+ mediadata);
                    break;
            }
        }
    }
    //上传图片到表中
    private void upload(String imgpath) {
        final BmobFile bmobFile = new BmobFile(new File(imgpath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    final User user =  BmobUser.getCurrentUser(User.class);
                    user.setHeadAvatar(bmobFile);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastUtils.show("上传文件成功");
                            } else {
                                ToastUtils.show("上传文件失败:"+e.getMessage());
                                Log.e("update", "上传文件失败: "+e.getMessage() );
                            }
                            
                        }
                    });
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    //Log.w("bbb",bmobFile.getFileUrl());
                } else {
                    ToastUtils.show("上传文件失败:"+e.getMessage());
                    Log.e("upload", "上传文件失败: "+e.getMessage() +":"+e.getErrorCode());
                }
            }

        });
    }

    private void setNickName() {
        Intent intent = new Intent(this, setNickNameActivity.class);
        startActivityForResult(intent,0);
    }

    private void setPhoneNumber() {
        Intent intent = new Intent(this, setPhoneActivity.class);
        startActivityForResult(intent,0);
    }

    private void setLocation() {
        Intent intent = new Intent(this, setLocationActivity.class);
        startActivityForResult(intent,0);
    }

    private void doChangePassword() {
        Intent intent = new Intent(this, changePasswordActivity.class);
        startActivityForResult(intent,0);
    }

    private void doOutLogin() {
        BmobUser.logOut();
    }

    private void showSexChooseDialog() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
        builder3.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                // showToast(which+"");
                sex_tv.setText(sexArry[which]);
                final User user =  BmobUser.getCurrentUser(User.class);
                user.setSex(sexArry[which].toString());
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            ToastUtils.show("保存成功");
                        }else{
                            ToastUtils.show("保存失败"+e.getMessage());
                        }
                    }
                });
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }
        });
        builder3.show();// 让弹出框显示
    }

    public void showHeadImg(){

        //下载图片
        BmobQuery<User> query=new BmobQuery<User>();
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){
                    show_ad(list);
                }else{
                    JUtils.Toast(""+e.getMessage());
                }
            }
        });
    }
    public void show_ad(List<User> list){
        User myUser = BmobUser.getCurrentUser(User.class);
        Log.i("Username", "getCurrentUser: "+ myUser.getUsername());

        for (User ad : list){
            Log.i("Username", "show_ad: "+ad.getUsername());
            if(ad.getUsername() != null && myUser.getUsername().equals(ad.getUsername())){
                BmobFile icon= ad.getHeadAvatar();
                icon.download(new DownloadFileListener() {
                    @Override
                    public void onProgress(Integer integer, long l) {
                        Log.i("Username", "onProgress "+ integer);
                    }
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            Log.i("Username", "uri: "+ s);
                            headImage.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
                        }else {
                            Log.i("Username", "BmobException: "+ e.getMessage());
                        }
                    }
                });
                break;
            }
        }

    }
}