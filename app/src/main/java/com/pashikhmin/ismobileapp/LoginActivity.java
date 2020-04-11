package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.exceptions.AuthenticationFailedException;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private SharedPreferences prefs;
    CredentialsResourceSupplier resourceSupplier;
    String cookie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(UserDetailsActivity.SHARED_PROPERTIES_NAME, MODE_PRIVATE);
        resourceSupplier = ((CredentialsResourceSupplier) Connectors.api());
        setTitle("Need Login");
        setContentView(R.layout.activity_login);
        findViewById(R.id.to_app).setOnClickListener(e -> onSubmitClick());
    }

    private void onSubmitClick() {
        final String login = ((TextView) findViewById(R.id.login_input)).getText().toString();
        final String password = ((TextView) findViewById(R.id.password_input)).getText().toString();

        findViewById(R.id.error_text).setVisibility(View.INVISIBLE);
        findViewById(R.id.progress_circular).setVisibility(View.VISIBLE);
        new LoadTask<>(e -> executeIntrospection(login, password), this::onCredentialLoaded).execute();
    }

    private String executeIntrospection(final String login, final String password) {
        try {
            String authCookie = resourceSupplier.getCookie(login, password);
            if (prefs.getBoolean(UserDetailsActivity.NOTIFICATION_TAG, true)) {
                Task<InstanceIdResult> task = FirebaseInstanceId.getInstance().getInstanceId();
                Tasks.await(task);
                String token = task.getResult().getToken();
                Connectors.api().sendFirebaseToken(token);
            }
            return authCookie;
        } catch (InterruptedException | ExecutionException | IOException e) {
            Log.e(TAG, "loadFacilitiesCallback: loadRegionsAndCategoriesCallback", e);
            throw new RuntimeException(e);
        }
    }

    private void onCredentialLoaded(LoadTaskResult<String> res) {
        findViewById(R.id.progress_circular).setVisibility(View.INVISIBLE);
        if (!res.successful()) {
            TextView tvError = findViewById(R.id.error_text);
            if (tvError.getVisibility() != View.VISIBLE)
                tvError.setVisibility(View.VISIBLE);
            int errorTextResource;
            if (res.getError().getCause() instanceof AuthenticationFailedException)
                errorTextResource = R.string.auth_error;
            else
                errorTextResource = R.string.unknown_error;
            tvError.setText(getString(errorTextResource));
            return;
        }
        cookie = res.getResult();
        new Handler().postAtTime(() -> toApp(), 1000);
    }

    private void toApp() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("token", cookie);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
