package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ismobileapp.R;
import com.pashikhmin.ismobileapp.network.LoadTask;
import com.pashikhmin.ismobileapp.network.LoginConnector;
import com.pashikhmin.ismobileapp.network.LoginWebClient;

import java.io.IOException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        LoadTask<?> loadTask = new LoadTask<>(x -> asyncLogin(), x -> {this.finishActivity(0);});
//        loadTask.execute();
        loginInWebView();
    }

    private void loginInWebView() {
        WebView webView = ((WebView) findViewById(R.id.webview));
        webView.loadUrl("http://192.168.43.56:8080/secure_ping");
    }

    private Object asyncLogin() {
        LoginConnector lc = new LoginConnector();
        try {
            Uri formUrl = lc.validateCredentialsUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW, formUrl);
            startActivityForResult(intent, 1);
            synchronized (lock) {
                lock.wait();
            }

            if (!lc.validCredentials())
                throw new RuntimeException("Credentials are not validating");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
