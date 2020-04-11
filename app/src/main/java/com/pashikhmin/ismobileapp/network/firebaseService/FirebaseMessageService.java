package com.pashikhmin.ismobileapp.network.firebaseService;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;

import java.io.IOException;

public class FirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageService";

    public FirebaseMessageService() {
        super();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        try {
            Log.i(TAG, "Trying to send new token: " + token);
            Connectors.api().sendFirebaseToken(token);
            Log.i(TAG, "Successfully sent new token");
        } catch (IOException e) {
            Log.e(TAG, "Could not send new token to api server", e);
        }
    }
}
