package com.example.helalert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHandler {

    private static final String CHANNEL_ID = "accident_alert_channel";
    private Context context;

    public NotificationHandler(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public void sendAccidentNotification() {
        // Intent to open AccidentResponseActivity when user clicks "YES, I am safe"
        Intent responseIntent = new Intent(context, AccidentResponseActivity.class);
        responseIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent responsePendingIntent = PendingIntent.getActivity(
                context,
                0,
                responseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_helmet) // replace with your app icon
                .setContentTitle("Are you safe?")
                .setContentText("Accident detected! Please confirm if you are safe.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_safe, "YES, I am safe", responsePendingIntent) // Action button
                .setContentIntent(responsePendingIntent); // Clicking notification also opens activity

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1001, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Accident Alerts";
            String description = "Alerts when accident is detected";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
