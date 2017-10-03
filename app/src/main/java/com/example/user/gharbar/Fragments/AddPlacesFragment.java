package com.example.user.gharbar.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.gharbar.R;

public class AddPlacesFragment extends Fragment {
   /* EditText placename,placeaddressline1,placeaddressline2,placecity,placestate,placebhk,placeprice;
    CheckBox swimmingpool,garden,lift,gym,cafeteria,apartment,house;
    String placeName,placeAddLine1,placeAddLine2,placeCity,placeState,placeBhk,placePrice;
    boolean Lift,SwimmingPool,Gym,Cafeteria,House,Apartment,Garden;*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_places, container, false);

/*
        placename=(EditText)v.findViewById(placename);
        placeaddressline1=(EditText)v.findViewById(R.id.placeaddressline1);
        placeaddressline2=(EditText)v.findViewById(R.id.placeaddressline2);
        placecity=(EditText)v.findViewById(R.id.placecity);
        placestate=(EditText)v.findViewById(R.id.placestate);
        placebhk=(EditText)v.findViewById(R.id.placebhk);
        placeprice=(EditText)v.findViewById(R.id.placeprice);
        swimmingpool=(CheckBox)v.findViewById(R.id.swimmingpool);
        garden=(CheckBox)v.findViewById(R.id.garden);
        lift=(CheckBox)v.findViewById(R.id.lift);
        cafeteria=(CheckBox)v.findViewById(R.id.cafeteria);
        gym=(CheckBox)v.findViewById(R.id.gym);
        apartment=(CheckBox)v.findViewById(R.id.apartment);
        house=(CheckBox)v.findViewById(R.id.house);






        return v;*/ }

  /*  public void Submit(View v){
        placeName=placename.getText().toString();
        placeAddLine1=placeaddressline1.getText().toString();
        placeAddLine2=placeaddressline2.getText().toString();
        placeCity=placecity.getText().toString();
        placeState=placestate.getText().toString();
        placeBhk=placebhk.getText().toString();
        placePrice=placeprice.getText().toString();
        Lift=lift.isChecked();
        SwimmingPool=swimmingpool.isChecked();
        Gym=gym.isChecked();
        Cafeteria=cafeteria.isChecked();
        Garden=garden.isChecked();
        Apartment=apartment.isChecked();
        House=house.isChecked();





        if(!placeName.matches("") && !placeAddLine1.matches("")&& !placeAddLine2.matches("")&& !placeCity.matches("")&& !placeState.matches("")&& !placeBhk.matches("") && !placePrice.matches("")){

        }
    }*/

}
