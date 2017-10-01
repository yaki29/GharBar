package com.example.user.gharbar.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.gharbar.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;
    EditText email,password;
    String Email,Pass,category;
    String passs,conp;
    TextView nota;
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button)findViewById(R.id.loginbtn);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        nota=(TextView)findViewById(R.id.textsignup);
        Email=email.getText().toString();
        Pass=password.getText().toString();
        login.setOnClickListener(this);
        nota.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.loginbtn){

            if(Email!=null && Pass!=null){
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

            }
            else if(Email==null && Pass==null){
                Toast.makeText(this, "Please Enter All the Details", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId()==R.id.textsignup){
            showAlertDialog();

        }
    }

    public void showAlertDialog()
    {
        final EditText name,email,pass,conpass;
         String Name,Ema;



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
}




