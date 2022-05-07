package com.example.reflex_game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RecieveScheduler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new NotificationScheduler(context).send("Itt az ideje játszani!");
    }
}