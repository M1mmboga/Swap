package com.example.swap;

import android.content.Intent;
import android.util.Log;

import com.example.swap.data.network.repository.UsersRepository;
import com.example.swap.models.User;
import com.example.swap.utils.Auth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static final String NOTIFICATION_BC = "com.johngachihi.example.swap.FCM_NOTIFICATION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendBroadcast(new Intent(NOTIFICATION_BC));
        Log.d(TAG, "From: " + remoteMessage.getFrom());

    }

    @Override
    public void onNewToken(String token) {
        UsersRepository usersRepository = new UsersRepository();

        User user = Auth.of(getApplication()).getCurrentUser();
        if(user != null) {
            usersRepository.putFCMInstanceIdForUser(user.getId(), token);
        }
    }

}
