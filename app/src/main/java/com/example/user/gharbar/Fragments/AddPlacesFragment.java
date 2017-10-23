package com.example.user.gharbar.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.gharbar.Models.Place;
import com.example.user.gharbar.R;
import com.example.user.gharbar.Utilities.PlaceModel;

import java.net.URISyntaxException;

public class AddPlacesFragment extends Fragment implements View.OnClickListener{
    Button submit,uploadphoto,uploaddocuments;
    EditText placeName,addLine1,addLine2,city,state,bhk,startingprice;
    CheckBox lift,gym,garden,swimmingpool,cafeteria,apartment,house;
    String proprieterID,absurd;
    boolean liftvalue=false ,gymvalue=false ,gardenvalue=false ,swimmingpoolvalue=false ,cafeteriavalue=false ,apartmentvalue=false ,housevalue=false;


    // Main data model object
    private static PlaceModel sTasks;
    //List<Place> allplaces;

    public static final String SETTINGS_CLOUDANT_USER1 = "33460cc3-8818-44e0-81f4-0021d7711652-bluemix";
    public static final String SETTINGS_CLOUDANT_DB1 = "places";
    public static final String SETTINGS_CLOUDANT_API_KEY1 = "tickentaidndroutheirifiv";
    public static final String SETTINGS_CLOUDANT_API_SECRET1 = "19618fb68f61d21893fb5f4c80b97a720b9f9141";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v("hello","OnCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(!haveNetworkConnection()){
            Toast.makeText(getContext(), "Please connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Rootview = inflater.inflate(R.layout.fragment_add_places, container, false);

        submit = (Button)Rootview.findViewById(R.id.submitbutton);
        placeName = (EditText)Rootview.findViewById(R.id.placeName);
        addLine1 = (EditText)Rootview.findViewById(R.id.addLine1);



        addLine1.setOnTouchListener(mtouchListener);

        city = (EditText)Rootview.findViewById(R.id.city);
        state = (EditText)Rootview.findViewById(R.id.state);
        bhk = (EditText)Rootview.findViewById(R.id.bhk);
        startingprice = (EditText)Rootview.findViewById(R.id.startingprice);
        lift = (CheckBox)Rootview.findViewById(R.id.lift);
        gym = (CheckBox)Rootview.findViewById(R.id.gym);
        garden = (CheckBox)Rootview.findViewById(R.id.garden);
        swimmingpool = (CheckBox)Rootview.findViewById(R.id.swimmingpool);
        cafeteria = (CheckBox)Rootview.findViewById(R.id.cafeteria);
        apartment = (CheckBox)Rootview.findViewById(R.id.apartment);
        house = (CheckBox)Rootview.findViewById(R.id.house);
        //uploaddocuments = (Button)Rootview.findViewById(R.id.uploaddocumentsbutton);
        //uploadphoto = (Button)Rootview.findViewById(R.id.uploadphotobutton);
        //uploaddocuments.setOnClickListener(this);
        submit.setOnClickListener(this);
        //uploadphoto.setOnClickListener(this);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("loggedIn info", Context.MODE_PRIVATE);
        proprieterID =  preferences.getString("email",null);


        if (sTasks == null) {
            // Model needs to stay in existence for lifetime of app.
            this.sTasks = new PlaceModel(this.getActivity());
        }

        return Rootview;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.submitbutton){

            if(placeName.getText().toString()!=null && addLine1.getText().toString()!=null && absurd!=null && city.getText().toString()!=null && state.getText().toString()!=null && bhk.getText().toString()!=null && startingprice.getText().toString()!=null){
                if(lift.isChecked())liftvalue =true;
                if(garden.isChecked())gardenvalue =true;
                if(gym.isChecked())gymvalue =true;
                if(swimmingpool.isChecked())swimmingpoolvalue =true;
                if(cafeteria.isChecked())cafeteriavalue =true;
                if(apartment.isChecked())apartmentvalue =true;
                if(house.isChecked())housevalue =true;
                if(!haveNetworkConnection()){
                    Toast.makeText(getContext(), "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }
                else{
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                createNewPlace(placeName.getText().toString(),addLine1.getText().toString(),absurd,city.getText().toString(),state.getText().toString(),bhk.getText().toString(),startingprice.getText().toString(),proprieterID,liftvalue,gardenvalue,swimmingpoolvalue,gymvalue,cafeteriavalue,housevalue,apartmentvalue);
                placeName.setText("");
                    bhk.setText("");
                    startingprice.setText("");
                    addLine1.setText("");
                    city.setText("");
                    state.setText("");
                    lift.setChecked(false);
                    gym.setChecked(false);
                    cafeteria.setChecked(false);
                    garden.setChecked(false);
                    apartment.setChecked(false);
                    house.setChecked(false);
                    swimmingpool.setChecked(false);


                }
            }
            else {
                Toast.makeText(getActivity(), "Please enter all the Details correctly", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void createNewPlace(String placeName, String addLine1, String addLine,String city,String state,String bhk,String startingPrice,String proprieterID,boolean lift,boolean garden,boolean swimmingpool,boolean gym,boolean cafeteria,boolean house,boolean apartment){
        Place t = new Place(placeName,addLine1,addLine,city,state,bhk,startingPrice,proprieterID,lift,garden,swimmingpool,gym,cafeteria,house,apartment);
        sTasks.createDocument(t);
        reloadReplicationSettings(0);



    }


    private void reloadReplicationSettings(int flag) {
        try {
            this.sTasks.reloadReplicationSettings(flag);
        } catch (URISyntaxException e) {
            Log.e("LOGINAC", "Unable to construct remote URI from configuration", e);
            Toast.makeText(getActivity(),
                    R.string.replication_error,
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
    private  View.OnTouchListener mtouchListener= new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                showChangeLangDialog();
            }
            return false;
        }
    };
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.screen_popup, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.addline1);
        final EditText edt1 = (EditText) dialogView.findViewById(R.id.addline2);

        dialogBuilder.setTitle("Custom dialog");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with
                Log.v("KO","KO");
                absurd = edt1.getText().toString();
                addLine1.setText(edt.getText().toString());

            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();    }

}
