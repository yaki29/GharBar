package com.example.user.gharbar.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.sync.documentstore.ConflictException;
import com.cloudant.sync.documentstore.DocumentStoreException;
import com.example.user.gharbar.Models.Place;
import com.example.user.gharbar.Models.User;
import com.example.user.gharbar.R;
import com.example.user.gharbar.Utilities.PlaceModel;
import com.example.user.gharbar.Utilities.UserModel;

import java.net.URISyntaxException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AlertDialog b;
    Button save,changepassword;
    boolean check;
    EditText Name,Email,Type,Address,Contact;
   private String passs,conp;
    String ProfileName,ProfileEmail,ProfileType,ProfileAddress,ProfileContact,ProfilePassword;
    int ProfileId;

    // Main data model object
    private static UserModel sTasks;
    public List<User> allusers;

    private static PlaceModel sTasksplace;
    public List<Place> allplaces;


    public static final String SETTINGS_CLOUDANT_USER = "33460cc3-8818-44e0-81f4-0021d7711652-bluemix";
    public static final String SETTINGS_CLOUDANT_DB = "user-tenant";
    public static final String SETTINGS_CLOUDANT_API_KEY = "wornindlyzenturnotognelo";
    public static final String SETTINGS_CLOUDANT_API_SECRET = "ab109a75705a0e942d478e261aae4f2bb79af6bb";




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        if (sTasks == null) {
            // Model needs to stay in existence for lifetime of app.
            this.sTasks = new UserModel(this.getActivity());
        }


        if (sTasksplace == null) {
            // Model needs to stay in existence for lifetime of app.
            this.sTasksplace = new PlaceModel(this.getActivity());
        }


        save = (Button)rootview.findViewById(R.id.Save);
        changepassword = (Button)rootview.findViewById(R.id.change_password);
        Name = (EditText)rootview.findViewById(R.id.Name);
        Email = (EditText)rootview.findViewById(R.id.Email);
        Type = (EditText)rootview.findViewById(R.id.type);
        Address = (EditText)rootview.findViewById(R.id.Address);
        Contact = (EditText)rootview.findViewById(R.id.contact);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("loggedIn info", Context.MODE_PRIVATE);
        ProfileId =  preferences.getInt("id",123);
        ProfileName = preferences.getString("name",null);
        ProfileEmail = preferences.getString("email",null);
        ProfileType = preferences.getString("category",null);
        ProfileAddress = preferences.getString("address",null);
        ProfileContact = preferences.getString("contact",null);

        Name.setText(ProfileName);
        Email.setText(ProfileEmail);
        Type.setText(ProfileType);
        Address.setText(ProfileAddress);
        Contact.setText(ProfileContact);

        reloadReplicationSettings(1);
        sTasksplace.startPullReplication();
        try {
            allusers = sTasks.allTasks();
            allplaces = sTasksplace.allTasks();
        } catch (DocumentStoreException e) {
            e.printStackTrace();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    User t = allusers.get(ProfileId);
                    t.setName(Name.getText().toString());
                    t.setEmail(Email.getText().toString());
                    t.setAddressline1(Address.getText().toString());
                    t.setContact(Contact.getText().toString());

                    if(ProfilePassword!=null)
                         t.setPassword(ProfilePassword);

                    t= sTasks.updateDocument(t);
                    allusers.set(ProfileId,t);
                    sTasks.startPushReplication();

                    if(ProfileType.equals("Proprietor")) {
                        for (int i = 0; i < allplaces.size(); i++) {
                            if(allplaces.get(i).getProprieterID().equals(ProfileEmail)){
                                Place p = allplaces.get(i);
                                p.setProprieterID(Email.getText().toString());
                                p=sTasksplace.updateDocument(p);
                                allplaces.set(i,p);
                            }
                        }
                    }

                    sTasksplace.startPushReplication();

                    Toast.makeText(getActivity().getBaseContext(),"Successfully Saved",Toast.LENGTH_SHORT).show();

                }
                catch (ConflictException e){

                } catch (DocumentStoreException e) {
                    e.printStackTrace();
                }
            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_pass, null);
//        final EditText to = (EditText) dialogView.findViewById(R.id.filterto);
//        final EditText from = (EditText) dialogView.findViewById(R.id.filterfrom);
        final EditText npass = (EditText)dialogView.findViewById(R.id.newPassword1);
       final EditText cpass = (EditText) dialogView.findViewById(R.id.confirmPassword1);
        //cpass.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        npass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                b.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passs=s.toString();
                b.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                passs=s.toString();
                check=checkPasswordMatch(passs,conp,cpass);

            }
        });
        cpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                b.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conp=s.toString();
                b.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                conp=s.toString();
                check=checkPasswordMatch(passs,conp,cpass);
            }
        });

        builder.setTitle("Change Password");
        builder.setView(dialogView);


        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            /*String City=city.getText().toString();
            int too=Integer.parseInt(to.getText().toString());
            int fromm=Integer.parseInt(from.getText().toString());*/

            public void onClick(DialogInterface dialog, int whichButton) {
                ProfilePassword = conp;
            }

        });

        b = builder.create();
        b.show();
        b.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

    }
    public boolean checkPasswordMatch(String s1,String s2,EditText ed){
        if(s1.equals(s2)&&!s1.equals("")){
            b.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.domain, 0, R.drawable.ic_check_black_24dp, 0);
            return true;
        }
        else{
            ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.domain, 0, 0, 0);
            return false;
        }
    }
}
