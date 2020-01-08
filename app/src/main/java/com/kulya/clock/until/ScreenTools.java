package com.kulya.clock.until;

import android.content.Context;

/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2020/1/8 10:11
*/
public class ScreenTools {
    /*dip转为px*/
    public static int dip2px(float dpValue) {
        final float scale = Myapplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip( float pxValue) {
        final float scale = Myapplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
