package com.goodroadbook.finedustsmartalert.server.cloudmessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.goodroadbook.finedustsmartalert.R;
import com.goodroadbook.finedustsmartalert.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        if (remoteMessage.getData().size() > 0) {
            Log.d("namjinha", "Message data payload: " + remoteMessage.getData());

            if (false)
            {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            }
            else
            {
                // Handle message within 10 seconds
                handleNow(remoteMessage);
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null)
            {
                Log.d("namjinha", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }
    }

    @Override
    public void onNewToken(String token)
    {
        Log.d("namjinha", "onNewToken token = " + token);
        sendRegistrationToServer(token);
    }

    private void scheduleJob()
    {
        Log.d("namjinha", "scheduleJob in");
    }

    private void handleNow(RemoteMessage remoteMessage)
    {
        Log.d("namjinha", "handleNow in");
        sendNotification(remoteMessage.getData().get("message"));
    }

    private void sendRegistrationToServer(String token)
    {
        Log.d("namjinha", "onNewToken token = " + token);
    }

    private void sendNotification(String messageBody)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "1000";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
