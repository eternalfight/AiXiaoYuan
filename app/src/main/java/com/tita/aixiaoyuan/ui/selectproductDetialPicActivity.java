package com.tita.aixiaoyuan.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.utils.GlideEngine;
import com.tita.aixiaoyuan.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class selectproductDetialPicActivity extends AppCompatActivity {
    @BindView(R.id.productDetial_pic)
    ImageView productDetial_pic;
    private String mediadata;
    private int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectproduct_detial_pic);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.btn_cancle,R.id.btn_finish,R.id.productDetial_pic})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_cancle:
                finish();
                break;
            case R.id.btn_finish:
                if (mediadata != null){
                    Intent intent = new Intent();
                    intent.putExtra("mediadata",mediadata);
                    intent.putExtra("width",size);
                    setResult(1,intent);
                    finish();
                }else {
                    ToastUtils.show("请选择图片");
                }

                break;
            case R.id.productDetial_pic:
                selectPic();
                break;
        }
    }
    private void selectPic() {
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
                .isEnableCrop(true)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
                .isAndroidQTransform(false)
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .compressSavePath(Utils.getImgPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .cutOutQuality(100)
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
                       /* Log.i("uploadpath", "是否压缩:" + media.isCompressed());
                        Log.i("uploadpath", "压缩:" + media.getCompressPath());
                        Log.i("uploadpath", "原图:" + media.getPath());
                        Log.i("uploadpath", "绝对路径:" + media.getRealPath());
                        Log.i("uploadpath", "是否裁剪:" + media.isCut());
                        Log.i("uploadpath", "裁剪:" + media.getCutPath());
                        Log.i("uploadpath", "是否开启原图:" + media.isOriginal());
                        Log.i("uploadpath", "原图路径:" + media.getOriginalPath());
                        Log.i("uploadpath", "Android Q 特有Path:" + media.getAndroidQToPath());
                        Log.i("uploadpath", "宽高: " + media.getWidth() + "x" + media.getHeight());
                        Log.i("uploadpath", "Size: " + media.getSize());*/
                        if (media.getCutPath() != null){
                            mediadata = media.getCutPath();
                            size = media.getWidth();
                        }
                        // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
                    }
                    if (selectList.size() != 0 ){
                        productDetial_pic.setImageURI(Uri.parse(selectList.get(0).getCompressPath()));
                        Utils.autoSizeImageViewHeight(this,productDetial_pic,size);
                    }
                    //upload(mediadata);
                    Log.i("uploadpath", "onActivityResult: "+ mediadata);
                    break;
            }
        }
    }



}