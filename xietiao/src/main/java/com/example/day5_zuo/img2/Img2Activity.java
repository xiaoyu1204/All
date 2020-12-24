package com.example.day5_zuo.img2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.day5_zuo.ImageLoader;
import com.example.day5_zuo.R;
import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.bean.WatermarkImage;

public class Img2Activity extends AppCompatActivity {

    private ImageView mShowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img2);
        initView();
    }

    private void initView() {
        mShowImage = (ImageView) findViewById(R.id.show_image);

        Bitmap bmm = ImageLoader.getIconBitmap(this,R.drawable.ic_watermark);
        WatermarkImage watermarkImage = new WatermarkImage(bmm) //bmm是添加的水印
                .setImageAlpha(80)      //图片透明度
                .setPositionX(0.5)      //X轴
                .setPositionY(0.5)      //Y轴
                .setRotation(15)        //旋转
                .setSize(0.3);          //大小

        WatermarkBuilder
                .create(this, R.drawable.sample_plot_3)     //要添加水印的图片
                .loadWatermarkImage(watermarkImage)
                .getWatermark()
                .setToImageView(mShowImage);            //显示的ImageView  ,控件

    }
}