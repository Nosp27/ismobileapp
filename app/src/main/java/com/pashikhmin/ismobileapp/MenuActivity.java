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
import java.util.Arrays;
import java.util.List;

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
        Button favoritesButton = findViewById(R.id.button_menu_favorites);
        Button aboutButton = findViewById(R.id.button_menu_about);

        List<Button> requireLogin = Arrays.asList(
                favoritesButton, helpdeskButton
        );

        facilitiesButton.setOnClickListener(e -> transitToActivity(MainActivity.class));
        userButton.setOnClickListener(e -> transitToActivity(UserDetailsActivity.class));
        favoritesButton.setOnClickListener(e -> transitToFavoriteInvestingFacilities());
        aboutButton.setOnClickListener(e -> transitToActivity(AboutActivity.class));
        helpdeskButton.setOnClickListener(e -> transitToActivity(HelpDeskActivity.class));


        for(Button btn : requireLogin) {
            if (Connectors.userAuthorized()) {
                btn.setEnabled(true);
                btn.setBackgroundTintList(ColorStateList.valueOf(
                        getResources().getColor(R.color.white, getTheme())
                ));
            } else {
                btn.setEnabled(false);
                btn.setBackgroundTintList(ColorStateList.valueOf(
                        getResources().getColor(R.color.colorDisabled, getTheme())
                ));
            }
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
