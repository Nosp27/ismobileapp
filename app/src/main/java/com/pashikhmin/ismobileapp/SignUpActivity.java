package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pashikhmin.ismobileapp.model.helpdesk.Actor;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.connectors.OktaResourceSupplier;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.to_app).setOnClickListener(e -> onDataEntered());
    }

    private void onDataEntered() {
        findViewById(R.id.error_text).setVisibility(View.INVISIBLE);
        findViewById(R.id.progress_circular).setVisibility(View.VISIBLE);

        Actor actor = new Actor();
        actor.setGivenName(((TextView)findViewById(R.id.first_name_input)).getText().toString());
        actor.setFamilyName(((TextView)findViewById(R.id.last_name_input)).getText().toString());
        actor.setEmail(((TextView)findViewById(R.id.login_input)).getText().toString());
        String password = ((TextView)findViewById(R.id.password_input)).getText().toString();
        String passordConfirm = ((TextView)findViewById(R.id.password_input2)).getText().toString();
        if(!passordConfirm.equals(password)) {
            error(getString(R.string.passwords_differ));
            return;
        }
        new LoadTask<>(x -> signInAsyncMethod(actor, password), this::onSignInFinished).execute();
    }

    private String signInAsyncMethod(Actor actor, String password) {
        try {
            return ((CredentialsResourceSupplier) Connectors.api()).signIn(actor, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onSignInFinished(LoadTaskResult<String> res) {
        findViewById(R.id.progress_circular).setVisibility(View.INVISIBLE);
        if(!res.successful()) {
            error(getString(R.string.unknown_error));
            return;
        }

        String cookie = res.getResult();
        new Handler().postAtTime(() -> toApp(cookie), 1000);
    }

    private void error(String errorMessage) {
        TextView errorText = findViewById(R.id.error_text);
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(errorMessage);
    }

    private void toApp(String cookie) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("token", cookie);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
