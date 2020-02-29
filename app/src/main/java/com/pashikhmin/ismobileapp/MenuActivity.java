package com.pashikhmin.ismobileapp;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bindButtons();
    }

    private void bindButtons() {
        Button facilitiesButton = findViewById(R.id.button_menu_facility);
        Button helpdeskButton = findViewById(R.id.button_menu_helpdesk);

        facilitiesButton.setOnClickListener(e -> transitToActivity(MainActivity.class));
        helpdeskButton.setOnClickListener(e -> transitToActivity(HelpDeskActivity.class));
    }

    private void transitToActivity(Class<? extends Activity> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }
}
