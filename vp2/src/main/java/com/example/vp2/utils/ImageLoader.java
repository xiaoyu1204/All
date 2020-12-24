package com.example.vp2.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vp2.app.Constants;
import com.example.vp2.app.MyApp;

public class ImageLoader {

    //url 图片地址
    public static void loadImage(String url, ImageView img){
        //用key为image的值的时候来判断当前时无图还有有图模式
        if( SpUtils.getInstance().getBoolean("image") && img != null){//无图模式
            Glide.with(MyApp.app).load(url).into(img);
        }else if(img != null){//有图模式
            Glide.with(MyApp.app).load(url).into(img);
        }
    }

     //解析图片的路径
    public static String[] splitUrl(String url){
        String[] arr = new String[3];
        //https://tpcdn.whfpsoft.com:443/File/headPhoto/20200404/fa5134d048f08eff6f3617dc35d3a836.jpg
        //到这个路径的 https://tpcdn.whfpsoft.com:443/
        int end = url.lastIndexOf("/")+1;
        //这个截取的 File/headPhoto/20200404/
        String baseUrl = url.substring(0,end);
        //fa5134d048f08eff6f3617dc35d3a836.jpg
        String imgName = url.substring(end,url.length());
        String path = Constants.PATH_IMGS+"/"+imgName;
        arr[0] = baseUrl;
        arr[1] = imgName;
        arr[2] = path;
        return arr;
    }
}
