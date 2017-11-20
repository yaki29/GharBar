package com.example.user.gharbar.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.gharbar.R;


public class DescriptionFragment extends Fragment {
    private  String Tenant;
    private  String proprietor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_description,container,false);
        Bundle bundle = getActivity().getIntent().getExtras();
        String Name = bundle.getString("Name");
        String Add = bundle.getString("Add");
//        Tenant = bundle.getString("Tenant");
         proprietor = bundle.getString("Proprietor");
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
        TextView book = (TextView)view.findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
       // profile.setText(bundle.getString("Proprietor"));
        return view;
    }
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.book, null);
        final TextView city = (TextView) dialogView.findViewById(R.id.pemail);
        city.setText(proprietor);
//        final EditText to = (EditText) dialogView.findViewById(R.id.filterto);
//        final EditText from = (EditText) dialogView.findViewById(R.id.filterfrom);
        builder.setTitle("Contact Details");
        builder.setView(dialogView);


        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            /*String City=city.getText().toString();
            int too=Integer.parseInt(to.getText().toString());
            int fromm=Integer.parseInt(from.getText().toString());*/
            public void onClick(DialogInterface dialog, int whichButton) {

            }

        });

        AlertDialog b = builder.create();
        b.show();


    }
}
