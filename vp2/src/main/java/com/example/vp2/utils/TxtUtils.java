package com.example.vp2.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.vp2.R;
import com.github.chrisbanes.photoview.PhotoView;


public class TxtUtils {
    //文字的封装
    public static void setTextView(TextView textView, String word){
        if(textView != null && !TextUtils.isEmpty(word)){
            textView.setText(word);
        }
    }

    public static void setImageView(ImageView image, String url){
        if(image != null && !TextUtils.isEmpty(url)){
            Glide.with(image).load(url).apply(new RequestOptions().bitmapTransform(new RoundedCorners(20))).into(image);
        }
    }

    public static void setImageView(Context context, ImageView imageView, String image) {
        if (imageView != null && !TextUtils.isEmpty(image)) {
            Glide.with(context).load(image).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(imageView);
        }
    }

    public static void setImageView(Context context, ImageView imageView, int image) {
        if (imageView != null) {
            Glide.with(context).load(image).into(imageView);
        }
    }

}











