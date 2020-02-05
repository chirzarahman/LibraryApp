package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password, conpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
//        conpass = findViewById(R.id.textInputConPassword);

        AndroidNetworking.initialize(getApplicationContext());
        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    public void register(View view) {
//        startActivity(new Intent(this, MainActivity.class));
        AndroidNetworking.post("http://192.168.6.99:8000/api/register")
                .addBodyParameter("name", name.getText().toString().trim())
                .addBodyParameter("email", email.getText().toString().trim())
                .addBodyParameter("password", password.getText().toString().trim())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.d("test", String.valueOf(response));
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("anError", error.getLocalizedMessage());
                    }
                });
//        finish();
    }
}
