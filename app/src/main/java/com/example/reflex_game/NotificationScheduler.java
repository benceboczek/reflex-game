package com.example.reflex_game;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;


public class NotificationScheduler {
    private static final String CHANNEL_ID = "reflex_game";

    private NotificationManager mNotifyManager;
    private Context mContext;


    public NotificationScheduler(Context context) {
        this.mContext = context;
        this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();


    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.d("GameActivity", "asd");
            return;
        }

        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "Reflex Játék értesítés", NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setDescription("Értseítés");

        mNotifyManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Reflex Játék")
                .setContentText(message)
                .setSmallIcon(R.drawable.ranklist_icon2)
                .setContentIntent(pendingIntent);


        mNotifyManager.notify(0, builder.build());
    }

}