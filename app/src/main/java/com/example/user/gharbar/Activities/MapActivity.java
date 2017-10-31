package com.example.user.gharbar.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.user.gharbar.Adapters.Fragment_Adapter;
import com.example.user.gharbar.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        Fragment_Adapter adapter = new Fragment_Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            Bundle bundle = getIntent().getExtras();
            LatLng latLng = new LatLng(27.552494, 76.631267);
            if(bundle.getDouble("Long")!=0 && bundle.getDouble("Lat")!=0){
            double lat = Double.parseDouble((String)bundle.get("Lat"));
            double lon = Double.parseDouble((String)bundle.get("Long"));
                if(lat!=0 && lon!=0){
                    latLng = new LatLng(lat, lon);
                }}


            googleMap.addMarker(new MarkerOptions().position(latLng).title("Alwar"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        }
    }


}
