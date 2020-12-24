package com.example.rich_text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_msg;
    private TextView tv_line;
    private LinearLayout layout;
    private TextView tv_img;
    private TextView tv_test;
    private TextView tv_test1;
    private TextView tv_test2;
    private TextView tv_test3;
    private TextView tv_test4;
    private TextView tv_test5;
    private TextView tv;
    private TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        testSpannable();
    }

    private void initView() {
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_line = (TextView) findViewById(R.id.tv_line);
        layout = (LinearLayout) findViewById(R.id.layout);
        tv_img = (TextView) findViewById(R.id.tv_img);
        tv_test = (TextView) findViewById(R.id.tv_test);
        tv_test1 = (TextView) findViewById(R.id.tv_test1);
        tv_test2 = (TextView) findViewById(R.id.tv_test2);
        tv_test3 = (TextView) findViewById(R.id.tv_test3);
        tv_test4 = (TextView) findViewById(R.id.tv_test4);
        tv_test5 = (TextView) findViewById(R.id.tv_test5);
        tv = (TextView) findViewById(R.id.content);
        mContent = (TextView) findViewById(R.id.content);
        mContent.setOnClickListener(this);
    }


    private void testSpannable() {
        initword(); //设置文字背景色

        inittest(); //设置文字前景色

        inittest1();//设置文字相对大小

        inittest2();//设置文字删除线

        inittest3();//设置文字下划线

        inittest4();//设置文字上标

        inittest5();//设置文字下标

        initimg();//在文本中添加表情

        initline();//设置文字可点击

        initcontext();//设置展开收起
    }

    private int maxLine = 3;
    private SpannableString elipseString;//收起的文字
    private SpannableString notElipseString;//展开的文字

    private void initcontext() {
        String content = "在全球，随着Flutter被越来越多的知名公司应用在自己的商业APP中，" +
                "Flutter这门新技术也逐渐进入了移动开发者的视野，尤其是当Google在2018年IO大会上发布了第一个" +
                "Preview版本后，国内刮起来一股学习Flutter的热潮。\n\n为了更好的方便帮助中国开发者了解这门新技术" +
                "，我们，Flutter中文网，前后发起了Flutter翻译计划、Flutter开源计划，前者主要的任务是翻译" +
                "Flutter官方文档，后者则主要是开发一些常用的包来丰富Flutter生态，帮助开发者提高开发效率。而时" +
                "至今日，这两件事取得的效果还都不错！";
        tv.setText(content);


        //获取TextView的画笔对象
        TextPaint paint = tv.getPaint();
        //每行文本的布局宽度
        int width = getResources().getDisplayMetrics().widthPixels - dip2px(this, 0);
        //实例化StaticLayout 传入相应参数
        StaticLayout staticLayout = new StaticLayout(content, paint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        //判断content是行数是否超过最大限制行数3行
        if (staticLayout.getLineCount() > maxLine) {
            //定义展开后的文本内容
            String string1 = content + "    收起";
            notElipseString = new SpannableString(string1);
            //给收起两个字设成蓝色
            notElipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#0079e2")), string1.length() - 2, string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //获取到第三行最后一个文字的下标
            int index = staticLayout.getLineStart(maxLine) - 1;
            //定义收起后的文本内容
            String substring = content.substring(0, index - 2) + "..." + "更多";
            elipseString = new SpannableString(substring);
            //给查看全部设成蓝色
            elipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#0079e2")), substring.length() - 5, substring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置收起后的文本内容
            tv.setText(elipseString);
            tv.setOnClickListener(this);
            //将textview设成选中状态 true用来表示文本未展示完全的状态,false表示完全展示状态，用于点击时的判断
            tv.setSelected(true);
        } else {
            //没有超过 直接设置文本
            tv.setText(content);
            tv.setOnClickListener(null);
        }

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context mContext, float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void initline() {//设置文字可点击
        //登录注册的阅读链接
        String txt = "我已阅读《网络安全手册》";  //检索
        //起始位置
        int startPos = txt.indexOf("《");
        //结束位置
        int endPos = txt.lastIndexOf("》") + 1;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(txt);

        //ForegroundColorSpan 设置文字的前景色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标 不包括终了下标
        stringBuilder.setSpan(foregroundColorSpan, startPos, endPos, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        //ClickableSpan，设置可点击的文本，设置这个属性的文本可以相应用户点击事件，
        // 至于点击事件用户可以自定义，就像效果图显示一样，用户可以实现点击跳转页面的效果
        //点击富文本
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //响应点击事件
                Toast.makeText(MainActivity.this, "跳转！= =加载中····", Toast.LENGTH_SHORT).show();
            }
        };

        stringBuilder.setSpan(clickableSpan, startPos, endPos, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        //使用ClickableSpan的文本如果想真正实现点击作用，必须为TextView设置setMovementMethod方法
        tv_line.setMovementMethod(LinkMovementMethod.getInstance());
        tv_line.setText(stringBuilder);
    }

    private void initimg() {//在文本中添加表情
        String msg = "你好！[机器人](机器人)";
        SpannableString spannableString = new SpannableString(msg);
        //drawable <-> bitmap <-> resouce  图片格式的转换
        /*Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        bmp.setWidth(20);
        bmp.setHeight(20);*/
        //添加富文本的图片
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        //控制图片上下左右
        drawable.setBounds(0, 0, 42, 42);
        //ImageSpan设置文字变为图片
        ImageSpan imageSpan = new ImageSpan(drawable);
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标 不包括终了下标
        int start = msg.indexOf("[");
        int end = msg.lastIndexOf("]") + 1;
        spannableString.setSpan(imageSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_img.setText(spannableString);
    }

    private void inittest5() {//设置文字下标
        SpannableString spannableString = new SpannableString("为文字设置下标");
        //SubscriptSpan，设置下标，功能与设置上标类似
        SubscriptSpan subscriptSpan = new SubscriptSpan();
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标 不包括终了下标
        spannableString.setSpan(subscriptSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_test5.setText(spannableString);
    }

    private void inittest4() {//设置文字上标
        SpannableString spannableString = new SpannableString("为文字设置上标");
        //SuperscriptSpan，设置上标
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标 不包括终了下标
        spannableString.setSpan(superscriptSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_test4.setText(spannableString);
    }

    private void inittest3() {//设置下划线
        SpannableString spannableString = new SpannableString("为文字设置下划线");
        //UnderlineSpan，为文本设置下划线
        UnderlineSpan underlineSpan = new UnderlineSpan();
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标 不包括终了下标
        spannableString.setSpan(underlineSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_test3.setText(spannableString);
    }

    private void inittest2() {//设置删除线
        SpannableString spannableString = new SpannableString("为文字设置删除线");
        //StrikethroughSpan，为文本设置中划线，也就是常说的删除线
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标 不包括终了下标
        spannableString.setSpan(strikethroughSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_test2.setText(spannableString);
    }

    private void inittest1() {
        //设置文字相对大小
        SpannableString spannableString = new SpannableString("文字的相对大小");
        //RelativeSizeSpan，设置文字相对大小，在TextView原有的文字大小的基础上，相对设置文字大小
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);//第一个字
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.4f);//第二个字
        RelativeSizeSpan sizeSpan03 = new RelativeSizeSpan(1.6f);//第三个字
        RelativeSizeSpan sizeSpan04 = new RelativeSizeSpan(1.8f);//第四个字
        RelativeSizeSpan sizeSpan05 = new RelativeSizeSpan(1.6f);//第五个字
        RelativeSizeSpan sizeSpan06 = new RelativeSizeSpan(1.4f);//第六个字
        RelativeSizeSpan sizeSpan07 = new RelativeSizeSpan(1.2f);//第七个字
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标 不包括终了下标
        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan03, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan04, 3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan05, 4, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan06, 5, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan07, 6, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_test1.setText(spannableString);
    }

    private void inittest() {
        //设置文字前景色
        //1 spannable
        SpannableString spannableString = new SpannableString("设置文字的前景色为淡蓝色");
        //ForegroundColorSpan 设置文字的前景色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标   从下标9开始 到这个字符串的最后总长度
        spannableString.setSpan(colorSpan, 9, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_test.setText(spannableString);

    }

    private void initword() {
        //设置文字背景色
        String word = "设置文字背景色";
        //1 spannable
        SpannableString spannableString = new SpannableString(word);
        //BackgroundColorSpan 设置文字背景色
        BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.RED);
        //Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
        spannableString.setSpan(colorSpan, 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        //赋值
        tv_msg.setText(spannableString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.content:
                if (v.isSelected()) {
                    //如果是收起的状态
                    tv.setText(notElipseString);
                    tv.setSelected(false);
                } else {
                    //如果是展开的状态
                    tv.setText(elipseString);
                    tv.setSelected(true);
                }
                break;
        }
    }
}
