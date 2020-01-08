package com.kulya.clock.activity;

import android.os.Bundle;

import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.kulya.clock.R;

import androidx.appcompat.app.AppCompatActivity;

public class setActivity extends AppCompatActivity implements ColorPickerDialogListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpage);
    }

    @Override
    public void onColorSelected(int dialogId, int color) {

    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
