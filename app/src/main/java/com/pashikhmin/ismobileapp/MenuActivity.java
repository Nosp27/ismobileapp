package com.pashikhmin.ismobileapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindButtons();
    }

    private void bindButtons() {
        Button facilitiesButton = findViewById(R.id.button_menu_facility);
        Button helpdeskButton = findViewById(R.id.button_menu_helpdesk);
        Button userButton = findViewById(R.id.button_menu_user);

        facilitiesButton.setOnClickListener(e -> transitToActivity(MainActivity.class));
        userButton.setOnClickListener(e -> transitToActivity(UserDetailsActivity.class));


        if (Connectors.userAuthorized()) {
            helpdeskButton.setOnClickListener(e -> transitToActivity(HelpDeskActivity.class));
            helpdeskButton.setEnabled(true);
            helpdeskButton.setBackgroundTintList(ColorStateList.valueOf(
                    getResources().getColor(R.color.white, getTheme())
            ));
        }
        else{
            helpdeskButton.setEnabled(false);
            helpdeskButton.setBackgroundTintList(ColorStateList.valueOf(
                    getResources().getColor(R.color.colorDisabled, getTheme())
            ));
        }
    }

    private void transitToActivity(Class<? extends Activity> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }
}
