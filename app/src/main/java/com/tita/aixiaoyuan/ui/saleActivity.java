package com.tita.aixiaoyuan.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.hjq.toast.ToastUtils;
import com.jude.utils.JUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureSelectorUIStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.tita.aixiaoyuan.Adapter.FullyGridLayoutManager;
import com.tita.aixiaoyuan.Adapter.GridImageAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.User;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.tita.aixiaoyuan.utils.GlideEngine;
import com.tita.aixiaoyuan.utils.Utils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class saleActivity extends AppCompatActivity {
    private static final String TAG = "saleActivity";
    @BindView(R.id.sale_cancle_btn)
    TextView sale_cancle_btn;
    @BindView(R.id.sale_publish)
    TextView sale_publish;
    @BindView(R.id.goods_titel)
    EditText goods_titel;
    @BindView(R.id.price_et)
    EditText price_et;
    @BindView(R.id.good_info)
    EditText good_info;
    @BindView(R.id.photo_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.type_tv)
    TextView type_tv;
    @BindView(R.id.produCut_et)
    EditText produCut_et;
    @BindView(R.id.productDetial_pic)
    ImageView productDetial_pic;

    private GridImageAdapter mAdapter;
    private int maxSelectNum = 6;
    private int themeId;
    private PictureParameterStyle mPictureParameterStyle;
    private PictureSelectorUIStyle mSelectorUIStyle;
    private PictureCropParameterStyle mCropParameterStyle;
    private List<String> tyepOptionsItems;
    OptionsPickerView pvTypeOptions;
    private String mediadata;

    private int product_type_int = 0;
    private String product_title;
    private String product_info;
    private int product_cunt;
    private double product_prise;
    private List<String> PicMap;  //商品图路径集合
    SweetAlertDialog pDialog;
    private int upLoadState = 0;//上传状态 0：
    private int upLoadMission = 0; //总上传任务数量;
    public static saleActivity instance = null;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // 被回收
        } else {
            clearCache();
        }
        setContentView(R.layout.activity_sale);
        ButterKnife.bind(this);
        instance = this;



        initpickType();
        themeId = R.style.picture_WeChat_style;
        mPictureParameterStyle = getWeChatStyle();
        //mSelectorUIStyle = PictureSelectorUIStyle.ofNewStyle();
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(getContext(), 8), false));
        mAdapter = new GridImageAdapter(getContext(), onAddPicClickListener);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapter.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }

        mAdapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapter.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(saleActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(saleActivity.this)
                                .externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;
                    default:
                        // 预览图片 可自定长按保存路径
//                        PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
//                        animationStyle.activityPreviewEnterAnimation = R.anim.picture_anim_up_in;
//                        animationStyle.activityPreviewExitAnimation = R.anim.picture_anim_down_out;
                        PictureSelector.create(saleActivity.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });

    }
    static Handler handler;
    @Override
    protected void onResume() {
        super.onResume();


    }


    private void getSelectPecture(int width) {
        Utils.autoSizeImageViewHeight(this,productDetial_pic,width);
        File file = new File(mediadata);
        Glide.with(this).load(file).into(productDetial_pic);
    }

    private productInfoBean productInfoBean;
    private String mObjectId;
    private void uploadData(){

        product_title = goods_titel.getText().toString().trim();
        product_info = good_info.getText().toString().trim();
        product_cunt = Integer.parseInt(produCut_et.getText().toString().trim());
        product_prise = Double.parseDouble(price_et.getText().toString().trim());

        if (product_title.isEmpty()||product_info.isEmpty()||price_et.getText().toString().isEmpty()){
            Toast.makeText(this,"请完善相关信息！",Toast.LENGTH_SHORT).show();
        }else {
            String id = Utils.genUniqueKeyProduct();
            productInfoBean = new productInfoBean();
            User user = BmobUser.getCurrentUser(User.class);
            productInfoBean.setUsername(user.getUsername());
            productInfoBean.setProduct_id(id);
            productInfoBean.setOne_category_id(product_type_int);
            productInfoBean.setCurrent_cnt(product_cunt);
            productInfoBean.setPrice(product_prise);
            productInfoBean.setProduct_name(product_title);
            productInfoBean.setPublish_status(0);
            productInfoBean.setProduct_info(product_info);

            String uri;
            switch (product_type_int) {
                case 0:
                    uri = "http://files.lunyong.top/2021/03/02/777eba36401691a980b69a61a0357464.png";
                    break;
                case 1:
                    uri = "http://files.lunyong.top/2021/03/02/74f6a62140c704e780c77c98eadec851.png";
                    break;
                case 2:
                    uri = "http://files.lunyong.top/2021/03/02/47f8298b40d2dcc780ac8870efa5cd48.png";
                    break;
                case 3:
                    uri = "http://files.lunyong.top/2021/03/02/23994e7340c664c280610332ed8aa683.png";
                    break;
                default:
                    uri = "http://files.lunyong.top/2021/03/02/777eba36401691a980b69a61a0357464.png";
                    break;
            }
            List<String> u = new ArrayList<>();
            u.add(uri);
            if (PicMap == null) productInfoBean.setPicUrl(u);
            upLoadMission++;

            productInfoBean.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        //JUtils.Toast("上传成功");
                        mObjectId = objectId;
                        Log.i(TAG, "done: 表创建成功");
                        upLoadState++;
                    } else {
                        Log.e("BMOB", e.toString());
                        JUtils.Toast("失败："+e.getMessage());
                        upLoadState++;
                    }
                }
            });

            if (mediadata != null){
                upLoadMission ++;
                final BmobFile bmobFile = new BmobFile(new File(mediadata));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            productInfoBean.setProduct_detial_pic(bmobFile);
                            updateData();
                            upLoadState++;
                        }else{
                            com.hjq.toast.ToastUtils.show("上传文件失败:" + e.getMessage());
                            Log.e("upload", "上传文件失败: " + e.getMessage() + ":" + e.getErrorCode());
                            upLoadState++;
                        }
                    }
                });
            }

        }
        //显示进度条
        pDialog = new SweetAlertDialog(saleActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在上传数据");
        pDialog.setCancelable(false);
        pDialog.show();

        List<String> PictureUrls;
        PictureUrls = new ArrayList<>();


        if (PicMap != null ){

            upLoadMission ++;
            //就路径存入数组
            final String[] filePaths = new String[PicMap.size()];
            int j = 0;
            for (String i : PicMap) {
                filePaths[j] = i;
                j++;
                //Log.i(TAG, "onActivityResult: "+i);
            }

            //批量上传图片
            BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

                @Override
                public void onSuccess(List<BmobFile> files,List<String> urls) {
                    //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                    //2、urls-上传文件的完整url地址
                    if(urls.size() == filePaths.length){//如果数量相等，则代表文件全部上传完成
                        productInfoBean.setPicUrl(urls);

                        //do something
                        // pDialog.dismiss();
                        for (String url: urls){
                            Log.i(TAG, "onSuccess: " + url);
                            PictureUrls.add(url);
                        }
                        switch (files.size()){

                            case 0:
                                break;
                            case 1:
                                productInfoBean.setPicOne(files.get(0));
                                break;
                            case 2:
                                productInfoBean.setPicOne(files.get(0));
                                productInfoBean.setPicTwo(files.get(1));
                                break;
                            case 3:
                                productInfoBean.setPicOne(files.get(0));
                                productInfoBean.setPicTwo(files.get(1));
                                productInfoBean.setPicThr(files.get(2));
                                break;
                            case 4:
                                productInfoBean.setPicOne(files.get(0));
                                productInfoBean.setPicTwo(files.get(1));
                                productInfoBean.setPicThr(files.get(2));
                                productInfoBean.setPicFor(files.get(3));
                                break;
                            case 5:
                                productInfoBean.setPicOne(files.get(0));
                                productInfoBean.setPicTwo(files.get(1));
                                productInfoBean.setPicThr(files.get(2));
                                productInfoBean.setPicFor(files.get(3));
                                productInfoBean.setPicFiv(files.get(4));
                                break;
                            case 6:
                                productInfoBean.setPicOne(files.get(0));
                                productInfoBean.setPicTwo(files.get(1));
                                productInfoBean.setPicThr(files.get(2));
                                productInfoBean.setPicFor(files.get(3));
                                productInfoBean.setPicFiv(files.get(4));
                                productInfoBean.setPicSix(files.get(5));
                                break;
                        }
                        updateData();
                        // pDialog.dismiss();
                        upLoadState++;
                    }
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    ToastUtils.show("错误码"+statuscode +",错误描述："+errormsg);
                    // pDialog.dismiss();
                    Log.i(TAG, "onError: "+errormsg);
                    upLoadState++;
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                    //1、curIndex--表示当前第几个文件正在上传
                    //2、curPercent--表示当前上传文件的进度值（百分比）
                    //3、total--表示总的上传文件数
                    //4、totalPercent--表示总的上传进度（百分比）
                    pDialog.getProgressHelper().setProgress(totalPercent);
                }
            });

        }
    }

    int mission = 0;
    private void upLoadDataRxJava(){

        // TODO: 2021/3/2 RxJava 批量删除数据
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                product_title = goods_titel.getText().toString().trim();
                product_info = good_info.getText().toString().trim();
                product_cunt = Integer.parseInt(produCut_et.getText().toString().trim());
                product_prise = Double.parseDouble(price_et.getText().toString().trim());

                if (product_title.isEmpty()||product_info.isEmpty()||price_et.getText().toString().isEmpty()){
                    ToastUtils.show("请完善相关信息！");
                    emitter.onComplete();
                }else {
                    String id = Utils.genUniqueKeyProduct();
                    productInfoBean = new productInfoBean();
                    User user = BmobUser.getCurrentUser(User.class);
                    productInfoBean.setUsername(user.getUsername());
                    productInfoBean.setProduct_id(id);
                    productInfoBean.setOne_category_id(product_type_int);
                    productInfoBean.setCurrent_cnt(product_cunt);
                    productInfoBean.setPrice(product_prise);
                    productInfoBean.setProduct_name(product_title);
                    productInfoBean.setPublish_status(0);
                    productInfoBean.setProduct_info(product_info);
                    Log.i(TAG, "uploadData: product_type_int"+ product_type_int );
                    String uri;
                    switch (product_type_int) {
                        case 0:
                            uri = "http://files.lunyong.top/2021/03/02/777eba36401691a980b69a61a0357464.png";
                            break;
                        case 1:
                            uri = "http://files.lunyong.top/2021/03/02/74f6a62140c704e780c77c98eadec851.png";
                            break;
                        case 2:
                            uri = "http://files.lunyong.top/2021/03/02/47f8298b40d2dcc780ac8870efa5cd48.png";
                            break;
                        case 3:
                            uri = "http://files.lunyong.top/2021/03/02/23994e7340c664c280610332ed8aa683.png";
                            break;
                        default:
                            uri = "http://files.lunyong.top/2021/03/02/47f8298b40d2dcc780ac8870efa5cd48.png";
                            break;
                    }
                    List<String> u = new ArrayList<>();
                    u.add(uri);
                    if (PicMap == null) productInfoBean.setPicUrl(u);
                    mission++;
                    productInfoBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            emitter.onNext(1);
                            if (e == null) {
                                mObjectId = objectId;
                                Log.i(TAG, "done: 表创建成功");
                            } else {
                                Log.e("BMOB", e.toString());
                                JUtils.Toast("失败："+e.getMessage());
                                emitter.onError(new Throwable(e.getMessage()));
                            }
                        }
                    });

                    if (mediadata != null){
                        mission++;
                        final BmobFile bmobFile = new BmobFile(new File(mediadata));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                emitter.onNext(2);
                                if (e == null){
                                    productInfoBean.setProduct_detial_pic(bmobFile);
                                }else{
                                    com.hjq.toast.ToastUtils.show("上传文件失败:" + e.getMessage());
                                    Log.e("upload", "上传文件失败: " + e.getMessage() + ":" + e.getErrorCode());
                                    emitter.onError(new Throwable(e.getMessage()));
                                }
                            }
                        });
                    }
                    List<String> PictureUrls = new ArrayList<>();
                    if (PicMap != null ){
                        //就路径存入数组
                        final String[] filePaths = new String[PicMap.size()];
                        int j = 0;
                        for (String i : PicMap) {
                            filePaths[j] = i;
                            j++;
                        }
                        //批量上传图片
                        mission++;
                        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                            @Override
                            public void onSuccess(List<BmobFile> files,List<String> urls) {
                                if(urls.size() == filePaths.length){
                                    emitter.onNext(3);
                                    productInfoBean.setPicUrl(urls);
                                    for (String url: urls){
                                        Log.i(TAG, "onSuccess: " + url);
                                        PictureUrls.add(url);
                                    }
                                    switch (files.size()){
                                        case 0:
                                            break;
                                        case 1:
                                            productInfoBean.setPicOne(files.get(0));
                                            break;
                                        case 2:
                                            productInfoBean.setPicOne(files.get(0));
                                            productInfoBean.setPicTwo(files.get(1));
                                            break;
                                        case 3:
                                            productInfoBean.setPicOne(files.get(0));
                                            productInfoBean.setPicTwo(files.get(1));
                                            productInfoBean.setPicThr(files.get(2));
                                            break;
                                        case 4:
                                            productInfoBean.setPicOne(files.get(0));
                                            productInfoBean.setPicTwo(files.get(1));
                                            productInfoBean.setPicThr(files.get(2));
                                            productInfoBean.setPicFor(files.get(3));
                                            break;
                                        case 5:
                                            productInfoBean.setPicOne(files.get(0));
                                            productInfoBean.setPicTwo(files.get(1));
                                            productInfoBean.setPicThr(files.get(2));
                                            productInfoBean.setPicFor(files.get(3));
                                            productInfoBean.setPicFiv(files.get(4));
                                            break;
                                        case 6:
                                            productInfoBean.setPicOne(files.get(0));
                                            productInfoBean.setPicTwo(files.get(1));
                                            productInfoBean.setPicThr(files.get(2));
                                            productInfoBean.setPicFor(files.get(3));
                                            productInfoBean.setPicFiv(files.get(4));
                                            productInfoBean.setPicSix(files.get(5));
                                            break;
                                    }

                                }
                            }
                            @Override
                            public void onError(int statuscode, String errormsg) {
                                ToastUtils.show("错误码"+statuscode +",错误描述："+errormsg);
                                Log.i(TAG, "onError: "+errormsg);
                                emitter.onError(new Throwable(errormsg));
                            }
                            @Override
                            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                                pDialog.getProgressHelper().setProgress(totalPercent);
                            }
                        });

                    }



                }

            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //显示进度条
                        pDialog = new SweetAlertDialog(saleActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("正在上传数据");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }

                    @Override
                    public void onNext(Integer s) {
                        Log.i("Observable", "onNext: "+ s );
                        count++;
                        if (count == mission){
                            updateData();
                            pDialog.dismiss();
                            do_preView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        pDialog.dismiss();
                        productInfoBean.delete(mObjectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.show("删除成功");
                                } else {
                                    Log.e("BMOB", e.toString());
                                }
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAG", "onComplete: ");
                    }
                });
    }
    int count = 0;

    //预览
    private void do_preView() {
        Intent intent = new Intent(this,ShopDetialActivity.class);
        intent.putExtra("GoodsObjectId",mObjectId);
        intent.putExtra("isPreView",1);
        startActivityForResult(intent,0);
    }

    private void updateData(){
        upLoadMission ++;
        productInfoBean.setOne_category_id(product_type_int);
        productInfoBean.update(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //ToastUtils.show("update：" + e.getMessage());
                    upLoadState++;
                } else {
                    //ToastUtils.show("update：" + e.getMessage());
                    Log.i(TAG, "update false: " + e.getMessage());
                    upLoadState++;
                }
            }
        });
    }

    private PictureParameterStyle getWeChatStyle() {
        // 相册主题
        PictureParameterStyle mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = true;
        // 状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册父容器背景色
        mPictureParameterStyle.pictureContainerBackgroundColor = ContextCompat.getColor(getContext(), R.color.app_color_black);
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_wechat_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_wechat_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_close;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册右侧按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_53575e);
        // 相册右侧按钮字体默认颜色
        mPictureParameterStyle.pictureRightDefaultTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_53575e);
        // 相册右侧按可点击字体颜色,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureRightSelectedTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureUnCompleteBackgroundStyle = R.drawable.picture_send_button_default_bg;
        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg;
        // 选择相册目录背景样式
        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_wechat_num_selector;
        // 相册标题背景样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatTitleBackgroundStyle = R.drawable.picture_album_bg;
        // 微信样式 预览右下角样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatChooseStyle = R.drawable.picture_wechat_select_cb;
        // 相册返回箭头 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatLeftBackStyle = R.drawable.picture_icon_back;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_grey);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_9b);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_53575e);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_half_grey);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(getContext(), R.color.app_color_white);
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");
        // 标题栏高度
        mPictureParameterStyle.pictureTitleBarHeight = ScreenUtils.dip2px(getContext(), 48);
        // 标题栏右侧按钮方向箭头left Padding
        mPictureParameterStyle.pictureTitleRightArrowLeftPadding = ScreenUtils.dip2px(getContext(), 3);

        // 裁剪主题
        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(getContext(), R.color.app_color_grey),
                ContextCompat.getColor(getContext(), R.color.app_color_grey),
                Color.parseColor("#393a3e"),
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                mPictureParameterStyle.isChangeStatusBarFontColor);

        return mPictureParameterStyle;
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            boolean mode = true;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(saleActivity.this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                        //.setPictureUIStyle(PictureSelectorUIStyle.ofNewStyle())  //mSelectorUIStyle
                        //.setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                        //.setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                        .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofCustomWindowAnimationStyle(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out))// 自定义相册启动退出动画
                        .isWeChatStyle(true)// 是否开启微信图片选择风格
                        .isUseCustomCamera(false)// 是否使用自定义相机
                        .setLanguage(LanguageConfig.CHINESE)// 设置语言，默认中文
                        .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                        .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
                        .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                        .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                        //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                        //.loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                        //.setOutputCameraPath(createCustomCameraOutPath())// 自定义相机输出目录
                        //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .maxVideoSelectNum(1) // 视频最大选择数量
                        //.minVideoSelectNum(1)// 视频最小选择数量
                        //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                        .imageSpanCount(4)// 每行显示个数
                        .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                        .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                        .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                        //.isAndroidQTransform(true)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                        .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                        //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                        //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                        //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                        //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                        //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                        //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                        .selectionMode( PictureConfig.MULTIPLE)// 多选 or 单选
                        .isPreviewImage(true)// 是否可预览图片
                        .isPreviewVideo(true)// 是否可预览视频
                        //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                        .isEnablePreviewAudio(true) // 是否可播放音频
                        .isCamera(true)// 是否显示拍照按钮
                        //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                        //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                        .isEnableCrop(true)// 是否裁剪
                        //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                        .isCompress(false)// 是否压缩
                        //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                        .synOrAsy(false)//同步true或异步false 压缩 默认同步
                        //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                        //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                        //.withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        //.isWebp(false)// 是否显示webp图片,默认显示
                        //.isBmp(false)//是否显示bmp图片,默认显示
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                        //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                        //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .isOpenClickSound(false)// 是否开启点击声音
                        .selectionData(mAdapter.getData())// 是否传入已选图片
                        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                        //.videoMinSecond(10)// 查询多少秒以内的视频
                        //.videoMaxSecond(15)// 查询多少秒以内的视频
                        //.recordVideoSecond(10)//录制视频秒数 默认60s
                        //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                        .cutOutQuality(90)// 裁剪输出质量 默认100
                        .minimumCompressSize(100)// 小于多少kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled(false) // 裁剪是否可旋转图片
                        //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                        .forResult(new MyResultCallback(mAdapter));
            } else {

            }
        }
    };
    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    private void clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureFileUtils.deleteAllCacheDirFile(getContext());
        } else {
            PermissionChecker.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

    /**
     * 返回结果回调
     */
    private class MyResultCallback implements OnResultCallbackListener<LocalMedia> {
        private WeakReference<GridImageAdapter> mAdapterWeakReference;


        public MyResultCallback(GridImageAdapter adapter) {
            super();
            this.mAdapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void onResult(List<LocalMedia> result) {
            PicMap = new ArrayList<>();
            for (LocalMedia media : result) {
          /*      Log.i(TAG, "是否压缩:" + media.isCompressed());
                Log.i(TAG, "压缩:" + media.getCompressPath());
                Log.i(TAG, "原图:" + media.getPath());
                Log.i(TAG, "绝对路径:" + media.getRealPath());
                Log.i(TAG, "是否裁剪:" + media.isCut());
                Log.i(TAG, "裁剪:" + media.getCutPath());
                Log.i(TAG, "是否开启原图:" + media.isOriginal());
                Log.i(TAG, "原图路径:" + media.getOriginalPath());
                Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
                Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
                Log.i(TAG, "Size: " + media.getSize());*/
                // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
                if (media.isOriginal()){
                    PicMap.add(media.getRealPath());
                }else {
                    PicMap.add(media.getCutPath());
                }
            }

            if (mAdapterWeakReference.get() != null) {
                mAdapterWeakReference.get().setList(result);
                mAdapterWeakReference.get().notifyDataSetChanged();
            }
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }
    @OnClick({R.id.sale_cancle_btn,R.id.sale_publish,R.id.type_lv,R.id.productDetial_pic})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.sale_cancle_btn:
                finish();
                break;
            case R.id.sale_publish:
                do_publish();

                break;
            case R.id.type_lv:
                pvTypeOptions.show();
                break;
            case R.id.productDetial_pic:
                selectproductDetial_pic();
                break;
        }
    }

    private void selectproductDetial_pic() {
        Intent intent = new Intent(this, selectproductDetialPicActivity.class);
        startActivityForResult(intent,1);
    }

    private void initpickType() {
        getData();
        pvTypeOptions = new OptionsPickerBuilder(saleActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = tyepOptionsItems.get(options1);
                type_tv.setText(tx);
            }
        }) .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                //String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                //JUtils.Toast(str);
                product_type_int = options1;
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("选择商品类型")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();
        pvTypeOptions.setPicker(tyepOptionsItems);//添加数据源
    }
    private void getData(){
        tyepOptionsItems = new ArrayList<>();
        tyepOptionsItems.add("购物");
        tyepOptionsItems.add("外卖");
        tyepOptionsItems.add("跑腿");
        tyepOptionsItems.add("闲置");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调

                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回五种path
                // 1.media.getPath(); 原图path
                // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                for (LocalMedia media : selectList) {
                    Log.i(TAG, "是否压缩:" + media.isCompressed());
                    Log.i(TAG, "压缩:" + media.getCompressPath());
                    Log.i(TAG, "原图:" + media.getPath());
                    Log.i(TAG, "绝对路径:" + media.getRealPath());
                    Log.i(TAG, "是否裁剪:" + media.isCut());
                    Log.i(TAG, "裁剪:" + media.getCutPath());
                    Log.i(TAG, "是否开启原图:" + media.isOriginal());
                    Log.i(TAG, "原图路径:" + media.getOriginalPath());
                    Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
                    Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
                    Log.i(TAG, "Size: " + media.getSize());

                    // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息


                }

                mAdapter.setList(selectList);
                mAdapter.notifyDataSetChanged();
            }
        }
        if(resultCode==1){
            mediadata = data.getStringExtra("mediadata");
            int width = data.getIntExtra("width",0);
            getSelectPecture(width);
        }
    }

    public void do_publish(){
        //uploadData();
        upLoadDataRxJava();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                while (upLoadMission != upLoadState){
                    continue;
                }
*//*                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*//*
                pDialog.dismiss();
                do_preView();
            }
        }).start();*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE) {// 存储权限
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    PictureFileUtils.deleteCacheDirFile(getContext(), PictureMimeType.ofImage());
                } else {
                    Toast.makeText(saleActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (BroadcastAction.ACTION_DELETE_PREVIEW_POSITION.equals(action)) {
                // 外部预览删除按钮回调
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    ToastUtils.show( "delete image index:" + position);
                    mAdapter.remove(position);
                    mAdapter.notifyItemRemoved(position);
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            BroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver,
                    BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
        }
    }
    public Context getContext() {
        return this;
    }
}
