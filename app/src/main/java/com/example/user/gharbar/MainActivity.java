package com.example.user.gharbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.user.gharbar.Activities.OptionActivity;

public class MainActivity extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setContentView(R.layout.activity_main);
        //   GLRippleView view=(GLRippleView)findViewById(R.id.rippleView);
        //  view.setRippleOffset(0.01f);
        ImageView logo=(ImageView)findViewById(R.id.logo);
        // logo.animate().alpha(0).setDuration(3000);
        logo.animate().scaleXBy(0.5f).scaleYBy(0.5f).setDuration(2000);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(MainActivity.this, OptionActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
