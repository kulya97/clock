package com.kulya.clock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.kulya.clock.R;
import com.kulya.clock.until.ScreenTools;

import androidx.appcompat.widget.AppCompatButton;

/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2020/1/8 9:56
*/
public class floatButton extends AppCompatButton {


    public floatButton(Context context) {
        this(context, null);
    }

    public floatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public floatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setBackgroundColor(int color) {
        //int strokeWidth =5;// 3dp 边框宽度
        // int roundRadius =15;// 8dp 圆角半径
        // int strokeColor = Color.parseColor("#33bb77");//边框颜色
        //  int fillColor = Color.parseColor("#33bb77");//内部填充颜色
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(color);
        gd.setCornerRadius(ScreenTools.dip2px(10));
        //gd.setStroke(strokeWidth, strokeColor);//描边
        //gd.setShape(1);  //1  圆形      把宽高设置一样就是圆形按钮了

        this.setBackgroundDrawable(gd);


    }


}

