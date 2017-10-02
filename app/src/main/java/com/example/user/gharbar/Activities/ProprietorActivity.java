package com.example.user.gharbar.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.gharbar.Fragments.AddPlacesFragment;
import com.example.user.gharbar.R;

public class ProprietorActivity extends AppCompatActivity {

    NavigationView navigationView;

    private DrawerLayout drawer;
    private View navHeader;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ProgressBar progressBar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    private static final String TAG_PROFILE = "profile";
    private static final String TAG_ADD_PLACE = "add place";
    private static final String TAG_ADD_BARBER = "add barber";
    private static final String TAG_MY_BOOKINGS = "my bookings";
    private static final String TAG_ABOUT_US = "about us";
    private static final String TAG_CONTACT_US = "contact us";
    private static final String TAG_LOGOUT = "logout";
    public static String CURRENT_TAG = TAG_PROFILE;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    String name;
    String category;

    SharedPreferences shared;
    ImageView glideImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proprietor);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

       /* progressBar=(ProgressBar)navHeader.findViewById(R.id.progressBar);
        glideImage=(ImageView)navHeader.findViewById(R.id.imageView);
        glideImage=(ImageView)navHeader.findViewById(R.id.imageView);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.type);*/
        if(!haveNetworkConnection()){
            Toast.makeText(this, "Please connect to the Internet", Toast.LENGTH_SHORT).show();
        }


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.proprietor_titles);
        shared=getSharedPreferences("loggedIn info", Context.MODE_PRIVATE);
        name=shared.getString("email","");
        category=shared.getString("servicetype","");
        // uid=shared.getString("uid","");



        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_PROFILE;
            loadHomeFragment();
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorWhite));


    }



    

    @Override
    protected void onResume() {
        super.onResume();
    /*    String imageUrl=shared.getString("image","");
        if(!imageUrl.equals(""))
        {
            Glide.with(getApplicationContext()).load(imageUrl)
                    .placeholder(R.drawable.face)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })

                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(glideImage);
        }  */


    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();


            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                AddPlacesFragment addplaces=new AddPlacesFragment();
                return addplaces;


            /*case 1:
                AddServiceFrag addService=new AddServiceFrag();
                return addService;
            case 2:
                AddBarberFragment addBarber=new AddBarberFragment();
                return addBarber;

            case 3:
                ProfileFragment profileFragment=new ProfileFragment();
                return profileFragment;


            case 4:
                AboutUsFragment aboutUs=new AboutUsFragment();
                return aboutUs;
            case 5:
                ContactUsFragment contactUs=new ContactUsFragment();
                return contactUs;*/



            default:
                return new AddPlacesFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.add_place:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    /*case R.id.add_service:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ADD_SERVICE;
                        break;
                    case R.id.add_barber:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ADD_BARBER;
                        break;
                    case R.id.my_bookings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_MY_BOOKINGS;
                        break;
                    case R.id.about_us:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ABOUT_US;
                        break;
                    case R.id.contact_us:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_CONTACT_US;
                        break;*/


                    case R.id.log_out:
                        SharedPreferences sharedPreferences = getSharedPreferences("loggedIn info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", "");
                        editor.putString("category","");
                        editor.putString("image","");
                        editor.putString("shopname","");
                        editor.apply();
                        startActivity(new Intent(ProprietorActivity.this, OptionActivity.class));

                        drawer.closeDrawers();
                        finish();
                        break;

                    default:
                        navItemIndex = 0;
                }
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_PROFILE;
                loadHomeFragment();
                return;
            }
            else {
                moveTaskToBack(true);
            }

        }



    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


}
