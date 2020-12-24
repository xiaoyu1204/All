package com.example.day5_zuo.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.day5_zuo.R;

import cn.walkpast.stamperlib.StampPadding;
import cn.walkpast.stamperlib.StampType;
import cn.walkpast.stamperlib.StampWatcher;
import cn.walkpast.stamperlib.Stamper;

public class ImgActivity extends AppCompatActivity {

    private ImageView mShowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        initView();
    }

    private void initView() {
        mShowImage = (ImageView) findViewById(R.id.show_image);

        mShowImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sample_plot_4));

        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.sample_plot_4);
        Bitmap watermark = BitmapFactory.decodeResource(getResources(), R.drawable.ic_watermark);
        Stamper.with(ImgActivity.this)
                .setMasterBitmap(bitmap4)
                .setWatermark(watermark)
                .setStampType(StampType.IMAGE)
                .setStampPadding(new StampPadding(bitmap4.getWidth() - watermark.getWidth() -80, 20))
                .setStampWatcher(mStampWatcher)
                .setRequestId(1002)
                .build();

    }

    StampWatcher mStampWatcher = new StampWatcher() {
        @Override
        protected void onSuccess(Bitmap bitmap, int requestId) {
            super.onSuccess(bitmap, requestId);

            switch (requestId) {

                case 1001:
                    //the result of text stamper

                    mShowImage.setImageBitmap(bitmap);

                    break;
                case 1002:
                    //the result of image stamper

                    mShowImage.setImageBitmap(bitmap);

                    break;
            }
        }

        @Override
        protected void onError(String error, int requestId) {
            super.onError(error, requestId);

            switch (requestId) {

                case 1001://

                    Toast.makeText(ImgActivity.this, "error:" + error, Toast.LENGTH_SHORT).show();

                    break;
                case 1002://

                    Toast.makeText(ImgActivity.this, "error:" + error, Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };


}