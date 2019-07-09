package com.example.swap.utils.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.swap.utils.NotificationHandler;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private NotificationHandler notificationHandler;

    public NotificationBroadcastReceiver(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHandler.handleNotification();
    }
}
