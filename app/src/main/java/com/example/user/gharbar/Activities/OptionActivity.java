package com.example.user.gharbar.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.user.gharbar.R;

public class OptionActivity extends AppCompatActivity {

   Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);



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
}
