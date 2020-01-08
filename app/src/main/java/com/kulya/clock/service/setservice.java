package com.kulya.clock.service;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import com.kulya.clock.until.Myapplication;
import com.kulya.clock.view.clock;

import java.time.Clock;

import androidx.annotation.RequiresApi;

/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2020/1/8 0:40
*/
@RequiresApi(api = Build.VERSION_CODES.N)
public class setservice extends TileService {


    @Override
    public void onStartListening() {
        super.onStartListening();
        getQsTile().setState(Tile.STATE_INACTIVE);
        getQsTile().updateTile();

    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();

        getQsTile().setState(Tile.STATE_ACTIVE);
        getQsTile().updateTile();
        Intent startService = new Intent(this, timeService.class);
        startService(startService);


}

}
