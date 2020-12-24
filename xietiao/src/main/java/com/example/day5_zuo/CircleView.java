package com.example.day5_zuo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.watermark.androidwm.Watermark;
import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.bean.WatermarkImage;
import com.watermark.androidwm.bean.WatermarkText;

import java.io.FileInputStream;

import static com.example.day5_zuo.R.mipmap.a;

public class CircleView extends View {

    private Paint paint; //画笔
    private int x,y;
    private int r = 50;

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    //Canvas 画纸
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //基本操作
        initView(canvas);

        //文字水印
        initText(canvas);



    }


    //基本操作
    private void initView(Canvas canvas) {
        //圆形
        x = r;
        y = r;
        canvas.drawCircle(x,y,r,paint);
//        文字
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        paint.setStrokeWidth(4);
        canvas.drawText("he1llo",20,20,paint);
//        图片
        final Bitmap bmm = ImageLoader.getIconBitmap(getContext(),R.mipmap.ic_launcher);
        canvas.drawBitmap(bmm,0,0,paint);
    }

    //图片水印

    //文字水印
    private void initText(Canvas canvas) {
        //开始绘制水印(文字水印)
        //获得页面尺寸
        int width = getWidth();
        int height = getHeight();

        //TextPaint是paint的子类,该类可以很方便的进行文字的绘制
        TextPaint textPaint = new TextPaint();
        textPaint.setARGB(120,223,22,28);   //设置水印颜色
        textPaint.setTextSize(30.0f);   //设置水印字体大小
        textPaint.setAntiAlias(true);   //抗锯齿
        //参数的意义分别为:文字内容,TextPaint对象,文本宽度,对齐方式,行距倍数,行距家属和是否包含内边距
        //这里比较重要的地方是设置文本宽度，当文本宽度比这个值大的时候就会自动换行
        String name = "小雨";     //文字内容
        StaticLayout layout = new StaticLayout(name, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);

        //同时
        float addWidth = width / 10,addHeight = height / 10;

        //水印的位置
//        float[] x = new float[]{width / 4-addWidth , width * 3 / 4-addWidth, width / 4-addWidth,  width* 3 / 4-addWidth};
//        float[] y = new float[]{height / 4-addHeight, height  / 4-addHeight, height*3 / 4-addHeight, height * 3 / 4-addHeight}; //水印的位置
        float[] x = new float[]{0, addWidth, addWidth * 5, 0, addWidth * 4, addWidth * 8, addWidth, addWidth * 5, addWidth * 9, addWidth * 4, addWidth * 8};
        float[] y = new float[]{addHeight, addHeight * 3, addHeight*3/2, addHeight*6,addHeight*9/2,addHeight*3,addHeight*17/2,addHeight*7,addHeight*11/2,addHeight*10,addHeight*17/2};

        //页面上绘制水印
        for (int i = 0; i <11 ; i++) {
            canvas.save();
            canvas.translate(x[i],y[i]);
            canvas.rotate(-30);
            layout.draw(canvas);
            canvas.restore();
        }
    }

}
