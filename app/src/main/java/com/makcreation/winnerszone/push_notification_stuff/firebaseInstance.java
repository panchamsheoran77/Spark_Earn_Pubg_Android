package com.makcreation.winnerszone.push_notification_stuff;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.app.Notification;

public class firebaseInstance extends FirebaseMessagingService {
    private NotificationManager notificationManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    displayNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }
    private void displayNotification(String title, String pass){
        notificationManager =   (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this,
                    "pushNotification")
                    .setContentTitle(title)
                    .setContentText(pass)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setChannelId("pushNotification")
                    .build();
            notificationManager.notify(101, notification);
        }else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setContentTitle(title);
            builder.setContentText(pass);
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
    }
}