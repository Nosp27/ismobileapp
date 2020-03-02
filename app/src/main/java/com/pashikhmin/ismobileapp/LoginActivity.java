package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.ProductionConnector;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static final String SERVERNAME = ProductionConnector.getServerAddress();
    private static final String TAG = "LoginActivity";
    CredentialsResourceSupplier resourceSupplier;
    String cookie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourceSupplier = ((CredentialsResourceSupplier) Connectors.getDefaultCachedConnector());
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
            return resourceSupplier.getCookie(login, password);
        } catch (IOException e) {
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

            tvError.setText(getString(R.string.auth_error));
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
