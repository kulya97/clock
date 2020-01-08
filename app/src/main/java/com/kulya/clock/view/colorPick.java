package com.kulya.clock.view;


import android.graphics.Color;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import androidx.fragment.app.FragmentActivity;

/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2019/9/28 18:21
*/
public class colorPick implements ColorPickerDialogListener {
    public static final int DIALOG_ID = 0;

    public colorPick(FragmentActivity context) {
        newDialog(context);
    }

    private void newDialog(FragmentActivity context) {

//传入的默认color
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setDialogId(DIALOG_ID)
                .setColor(Color.BLACK)
                .setShowAlphaSlider(true)
                .show(context);
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case DIALOG_ID:
                // We got result from the dialog that is shown when clicking on the icon in the action bar.
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
