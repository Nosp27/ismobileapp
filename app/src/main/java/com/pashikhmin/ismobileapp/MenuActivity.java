package com.pashikhmin.ismobileapp;

import android.app.Activity;
import android.app.Application;
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
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;

import java.io.Serializable;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "Token is: " + token;
                        Log.d(TAG, msg);
                        Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
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
        Button favoritesButton = findViewById(R.id.button_menu_favorites);

        facilitiesButton.setOnClickListener(e -> transitToActivity(MainActivity.class));
        userButton.setOnClickListener(e -> transitToActivity(UserDetailsActivity.class));
        favoritesButton.setOnClickListener(e -> transitToFavoriteInvestingFacilities());


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

    private void transitToFavoriteInvestingFacilities() {
        Criteries criteries = new Criteries();
        criteries.onlyLiked = true;

        Intent intent = new Intent(this, ActivityInvestingFacilities.class);
        intent.putExtra(MainActivity.CRITERIAS_TAG, criteries);
        intent.putExtra("custom_header", getString(R.string.favorites).toLowerCase());
        startActivity(intent);
    }
}
