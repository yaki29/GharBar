package com.example.user.gharbar.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.gharbar.R;


public class DescriptionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_description,container,false);
        Bundle bundle = getActivity().getIntent().getExtras();
        String Name = bundle.getString("Name");
        String Add = bundle.getString("Add");
        String Price = bundle.getString("Price");
        Boolean gym = bundle.getBoolean("Gym");
        Boolean swimmingPool = bundle.getBoolean("Garden");
        Boolean garden = bundle.getBoolean("Lift");
        Boolean cafeteria = bundle.getBoolean("Cafeteria");
        TextView name = (TextView)view.findViewById(R.id.Name_des);
        TextView add = (TextView)view.findViewById(R.id.Add_des);
        TextView price = (TextView)view.findViewById(R.id.Price_des);
        add.setText(Add);
        price.setText(Price);
        name.setText(Name);
        TextView Gym = (TextView)view.findViewById(R.id.gym);
        Gym.setVisibility(View.GONE);
        TextView SwimmingPool = (TextView)view.findViewById(R.id.swimmingpool);
        SwimmingPool.setVisibility(View.GONE);
        TextView Garden = (TextView)view.findViewById(R.id.garden);
        Garden.setVisibility(View.GONE);
        TextView Lift = (TextView)view.findViewById(R.id.lift);
        Lift.setVisibility(View.GONE);
        TextView Cafetria = (TextView)view.findViewById(R.id.Cafeteria);
        Cafetria.setVisibility(View.GONE);
        if(gym)
            Gym.setVisibility(View.VISIBLE);
        if(swimmingPool)
            SwimmingPool.setVisibility(View.VISIBLE);
        if(garden)
            Garden.setVisibility(View.VISIBLE);
        if(cafeteria)
            Cafetria.setVisibility(View.VISIBLE);

        return view;
    }

}
