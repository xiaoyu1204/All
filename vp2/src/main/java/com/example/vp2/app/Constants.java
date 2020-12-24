package com.example.vp2.app;

import java.io.File;

public class Constants {
    //网络缓存的地址
    public static final String PATH_DATA = MyApp.app.getCacheDir().getAbsolutePath() + File.separator + "data";
    //拼接一个地址
    public static final String PATH_IMGS = PATH_DATA + "/tp/imgs";
}
