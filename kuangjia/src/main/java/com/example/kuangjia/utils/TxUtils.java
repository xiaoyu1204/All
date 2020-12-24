package com.example.kuangjia.utils;

import android.text.TextUtils;
import android.widget.TextView;

public class TxUtils {

    public static void setTextView(TextView textView,String word){
        if(textView != null && !TextUtils.isEmpty(word)){
            textView.setText(word);
        }
    }

}
