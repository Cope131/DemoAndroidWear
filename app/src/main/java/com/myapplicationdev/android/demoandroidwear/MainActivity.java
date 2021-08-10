package com.myapplicationdev.android.demoandroidwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    final int NOTIFICATION_ID = 001; // A unique ID for our notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickNotify(View view) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);

        // (1) Destination
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentReply = new Intent(MainActivity.this, ReplyActivity.class);
        PendingIntent pendingIntentReply = PendingIntent.getActivity(
                MainActivity.this, 0, intentReply, PendingIntent.FLAG_UPDATE_CURRENT);

        // (2) Create
        // Actions
        // -> This is an Action Button
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher, "This is an Action", pendingIntent)
                .build();

        // Choices when reply is pressed
        // Get selected item with the key status using getIntent
        RemoteInput ri = new RemoteInput.Builder("status")
                .setLabel("Status report")
                .setChoices(new String [] {"Done", "Not yet"})
                .build();

        // -> Reply Action Button
        NotificationCompat.Action action2 = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Reply", pendingIntentReply)
                .addRemoteInput(ri)
                .build();

        // Add Actions
        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender()
                .addAction(action)
                .addAction(action2);

        // Notification Properties
        String text = getString(R.string.basic_notify_msg);
        String title = getString(R.string.notification_title);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "default")
                .setContentText(text)
                .setContentTitle(title)
                .setSmallIcon(android.R.drawable.btn_star_big_off)
                .extend(extender);
        // (3)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is for default notification");
            nm.createNotificationChannel(channel);
        }

        nm.notify(NOTIFICATION_ID,  builder.build());

    }
}