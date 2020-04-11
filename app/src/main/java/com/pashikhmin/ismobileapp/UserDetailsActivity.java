package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pashikhmin.ismobileapp.model.helpdesk.Actor;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.exceptions.LoginRequiredException;
import com.pashikhmin.ismobileapp.network.firebaseService.FirebaseMessageService;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {
    private Actor me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.layout_loading);
        new LoadTask<>(e -> getUserProfileDetailsAsync(), this::onUserResolved).execute();
    }

    private Actor getUserProfileDetailsAsync() {
        try {
            return Connectors.api().finger();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onUserResolved(LoadTaskResult<Actor> res) {
        if(!res.successful()) {
            onLoadError(res.getError());
            return;
        }
        me = res.getResult();
        setContentView(R.layout.activity_user_details);
        renderProperties();
    }

    private void renderProperties() {
        findViewById(R.id.back).setOnClickListener(e -> onBackPressed());
        ((TextView) findViewById(R.id.tv_user_name)).setText(me.getFullName());

        LinearLayout propertyContainer = findViewById(R.id.property_layout);

        initNotificationSetting();

        LinkedHashMap<String, String> properties = new LinkedHashMap<>();
        properties.put(getString(R.string.given_name), me.getGivenName());
        properties.put(getString(R.string.family_name), me.getFamilyName());
        properties.put(getString(R.string.email), me.getEmail());
        properties.forEach(
                (key, value) -> {
                    View propertyView = getLayoutInflater()
                            .inflate(R.layout.facility_detailed_property, propertyContainer);
                    propertyContainer.addView(propertyView);
                    ((TextView) propertyView.findViewById(R.id.property_header)).setText(key);
                    ((TextView) propertyView.findViewById(R.id.property_value)).setText(value);
                }
        );

    }

    private void initNotificationSetting() {
        Switch notificationSwitch = findViewById(R.id.notification_switch);
        notificationSwitch.setOnCheckedChangeListener(
                (view, isChecked) -> {
                    if (isChecked)
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("APP");
                    else FirebaseMessaging.getInstance().subscribeToTopic("APP");
                }
        );
    }

    private void onLoadError(Throwable throwable) {
        if (throwable instanceof LoginRequiredException || throwable.getCause() instanceof LoginRequiredException) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, 1);
        } else
            showErrorScreen();
    }

    private void showErrorScreen() {
        setContentView(R.layout.error_screen);
        Button retryBtn = findViewById(R.id.retry_button);
        retryBtn.setOnClickListener(e -> onResume());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Connectors.setAuthenticityToken(data.getStringExtra("token"));
            }
        }
    }
}
