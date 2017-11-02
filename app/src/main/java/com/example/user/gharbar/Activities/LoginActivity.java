package com.example.user.gharbar.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.sync.documentstore.DocumentStoreException;
import com.example.user.gharbar.Models.User;
import com.example.user.gharbar.R;
import com.example.user.gharbar.Utilities.UserModel;

import java.net.URISyntaxException;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static UserModel sTasks;
    Button login;
    EditText email,password;
    String Email,Pass,category;
    String passs,conp;
    TextView nota;
    boolean check;
    private ProgressDialog progressDialog;

    public static final String SETTINGS_CLOUDANT_USER = "33460cc3-8818-44e0-81f4-0021d7711652-bluemix";
    public static final String SETTINGS_CLOUDANT_DB = "user-tenant";
    public static final String SETTINGS_CLOUDANT_API_KEY = "wornindlyzenturnotognelo";
    public static final String SETTINGS_CLOUDANT_API_SECRET = "ab109a75705a0e942d478e261aae4f2bb79af6bb";



    // Main data model object

    List<User> allusers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        login=(Button)findViewById(R.id.loginbtn);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        nota=(TextView)findViewById(R.id.textsignup);
        Email=email.getText().toString();
        Pass=password.getText().toString();
        login.setOnClickListener(this);
        nota.setOnClickListener(this);

       // checkForLogin();
        // Protect creation of static variable.
        if (sTasks == null) {
            // Model needs to stay in existence for lifetime of app.
            this.sTasks = new UserModel(this.getApplicationContext());
        }

  //   this.sTasks.setReplicationListener(this);
     //   this.reloadTasksFromModel();
    }

    private void reloadTasksFromModel() {

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
    @Override
    public void onClick(View v) {
        progressDialog.show();
        //sTasks.startPullReplication();
        reloadReplicationSettings(1);
        if(v.getId()==R.id.loginbtn){

            Email=email.getText().toString();
            Pass=password.getText().toString();

            if(Email!=null && Pass!=null){


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int a=1;
                        progressDialog.dismiss();


                        try {
                            allusers=sTasks.allTasks();

                            for(int i=0;i<allusers.size();i++){
                                if(allusers.get(i).getEmail().equals(Email) && allusers.get(i).getPassword().equals(Pass)){
                                    //Toast.makeText(this, "Login successfull", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loggedIn info", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", allusers.get(i).getEmail());
                                    editor.putInt("id",i);
                                    editor.putString("name",allusers.get(i).getName());
                                    editor.putString("category",allusers.get(i).getCategory());
                                    editor.putString("address",allusers.get(i).getAddressline1());
                                    editor.putString("contact",allusers.get(i).getContact());
                                   // Toast.makeText(this, allusers.get(i).getCategory()+allusers.get(i).getId(), Toast.LENGTH_SHORT).show();


                                    editor.commit();


                                    if(allusers.get(i).getCategory().equals("Tenant")){
                                        a=0;
                                        Intent intent = new Intent(getApplicationContext(), TenantActivity.class);

                                        startActivity(intent);


                                    }
                                    else{
                                        a=0;
                                        Intent intent = new Intent(getApplicationContext(), ProprietorActivity.class);

                                        startActivity(intent);
                                    }
                                }
                            }
                            if(a==1)
                            Toast.makeText(getApplicationContext(),"Please check entered details\n TRY AGAIN",Toast.LENGTH_SHORT).show();
                        } catch (DocumentStoreException e) {
                            e.printStackTrace();
                        }
                    }
                },10000);
//                try {
//                    allusers=sTasks.allTasks();
//
//                    for(int i=0;i<allusers.size();i++){
//                        if(allusers.get(i).getEmail().equals(Email) && allusers.get(i).getPassword().equals(Pass)){
//                            Toast.makeText(this, "Login successfull", Toast.LENGTH_SHORT).show();
//                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loggedIn info", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("email", allusers.get(i).getEmail());
//                            editor.putString("id",allusers.get(i).getId());
//                            editor.putString("category",allusers.get(i).getCategory());
//                            Toast.makeText(this, allusers.get(i).getCategory()+allusers.get(i).getId(), Toast.LENGTH_SHORT).show();
//
//
//                            editor.commit();
//
//                            if(allusers.get(i).getCategory().equals("Tenant")){
//
//                                Intent intent = new Intent(getApplicationContext(), TenantActivity.class);
//
//                                startActivity(intent);
//
//                            }
//                            else{
//                                Intent intent = new Intent(getApplicationContext(), ProprietorActivity.class);
//
//                                startActivity(intent);
//                            }
//                        }
//                    }
//                } catch (DocumentStoreException e) {
//                    e.printStackTrace();
//                }

            }
            else if(Email==null && Pass==null){
                Toast.makeText(this, "Please Enter All the Details", Toast.LENGTH_SHORT).show();
            }
        }
         if(v.getId()==R.id.textsignup){
            showAlertDialog();

        }
    }

    public void showAlertDialog()
    {
        progressDialog.dismiss();
        final EditText name,email,pass,conpass;
         final String Name,Ema;



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
         LayoutInflater inflater = this.getLayoutInflater();
        final View dv = inflater.inflate(R.layout.sign_up_dialog, null);
        name=(EditText)dv.findViewById(R.id.Name);
        email=(EditText)dv.findViewById(R.id.Email);
        pass=(EditText)dv.findViewById(R.id.Password);
        conpass=(EditText)dv.findViewById(R.id.confirmPassword);
        conpass.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        builder.setView(dv);

        Name=name.getText().toString();
        Ema=email.getText().toString();

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passs=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
              passs=s.toString();
                check=checkPasswordMatch(passs,conp,conpass);

            }
        });

        conpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conp=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                conp=s.toString();
                check=checkPasswordMatch(passs,conp,conpass);
            }
        });


        Spinner spinner = (Spinner) dv.findViewById(R.id.category_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Users, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(LoginActivity.this, "Please Select Your Category", Toast.LENGTH_SHORT).show();
            }
        });



        builder.setTitle("Sign Up");

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {

        if(name.getText().toString()!=null && email.getText().toString()!=null && pass.getText().toString()!=null && conpass.getText().toString()!=null && check==true){



            Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
            createNewUser(name.getText().toString(),email.getText().toString(),pass.getText().toString(),category);


        }
        else {
            showAlertDialog();
            Toast.makeText(LoginActivity.this, "Please enter all the Details correctly", Toast.LENGTH_SHORT).show();

        }


        }
    });





        AlertDialog b = builder.create();
        b.show();
    }



    public boolean checkPasswordMatch(String s1,String s2,EditText ed){
        if(s1.equals(s2)){
            ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
            return true;
        }
        else{
            ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return false;
        }
    }
    private void createNewUser(String name,String email,String password,String Category) {
        User t = new User(name,email,password,Category);
        sTasks.createDocument(t);
        reloadReplicationSettings(0);



    }


    private void reloadReplicationSettings(int flag) {
        try {
            this.sTasks.reloadReplicationSettings(flag);
        } catch (URISyntaxException e) {
            Log.e("LOGINAC", "Unable to construct remote URI from configuration", e);
            Toast.makeText(getApplicationContext(),
                    R.string.replication_error,
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        this.sTasks.setReplicationListener(this);
        //stask.startPullReplication();
        // Load the tasks from the model
        checkForLogin();
        this.reloadTasksFromModel();
    }

    @Override
    public void onBackPressed() {
        checkForLogin();
       // super.onBackPressed();

    }
}




