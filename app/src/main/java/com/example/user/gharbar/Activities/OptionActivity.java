package com.example.user.gharbar.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.example.user.gharbar.R;

public class OptionActivity extends AppCompatActivity {

   Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

            checkForLogin();


    }
    public void Search(View v){

        i = new Intent(getApplicationContext(), LoginActivity.class);
        i.putExtra("type","tenant");
        startActivity(i);
    }

    public void Tolet(View v){

        i = new Intent(getApplicationContext(), LoginActivity.class);
        i.putExtra("type","proprietor");
        startActivity(i);
    }

    private void checkForLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("loggedIn info", Context.MODE_PRIVATE);

        if (!TextUtils.isEmpty(sharedPreferences.getString("category", ""))) {
            if (sharedPreferences.getString("category", "").equals("Proprietor")) {
                startActivity(new Intent(this, ProprietorActivity.class));
                finish();
            }

             else if (sharedPreferences.getString("category","").equals("Tenant")) {
                startActivity(new Intent(this, TenantActivity.class));
                finish();
        }
            //} end of chk for homeservice
        }}
}
