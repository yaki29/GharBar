package com.example.user.gharbar.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.gharbar.Models.Place;
import com.example.user.gharbar.Models.ServerResponse;
import com.example.user.gharbar.R;
import com.example.user.gharbar.Utilities.APIService;
import com.example.user.gharbar.Utilities.ApiClient;
import com.example.user.gharbar.Utilities.FilePath;
import com.example.user.gharbar.Utilities.GeocodingLocation;
import com.example.user.gharbar.Utilities.PlaceModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.net.URISyntaxException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddPlacesFragment extends Fragment implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    LocationManager locationManager;
    private static final int PICK_PDF_REQUEST = 1 ;
    private Uri filePath;
    private static final int STORAGE_PERMISSION_CODE = 123;
    public static final String UPLOAD_URL = "http://eventapp.000webhostapp.com/presenterPDF.php";
    Button submit,picUpload,uploaddocuments;
    EditText placeName,addLine1,addLine2,city,state,bhk,startingprice;
    CheckBox lift,gym,garden,swimmingpool,cafeteria,apartment,house;
    String proprieterID,absurd,mediaPath,nameImage,imageUrl;
    public Double latitude;
    public Double longitude;
    boolean liftvalue=false ,gymvalue=false ,gardenvalue=false ,swimmingpoolvalue=false ,cafeteriavalue=false ,apartmentvalue=false ,housevalue=false;
    ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mlocation;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    int flag=0,docFlag=0;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    AlertDialog b;
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
        progressDialog=new ProgressDialog(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Rootview = inflater.inflate(R.layout.fragment_add_places, container, false);

        submit = (Button)Rootview.findViewById(R.id.submitbutton);
        placeName = (EditText)Rootview.findViewById(R.id.placeName);
        addLine1 = (EditText)Rootview.findViewById(R.id.addLine1);
        picUpload = (Button)Rootview.findViewById(R.id.btnpic);


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
        uploaddocuments = (Button)Rootview.findViewById(R.id.btndoc);
        //uploadphoto = (Button)Rootview.findViewById(R.id.uploadphotobutton);
        uploaddocuments.setOnClickListener(this);
        submit.setOnClickListener(this);
        picUpload.setOnClickListener(this);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("loggedIn info", Context.MODE_PRIVATE);
        proprieterID =  preferences.getString("email",null);


        if (sTasks == null) {
            // Model needs to stay in existence for lifetime of app.
            this.sTasks = new PlaceModel(this.getActivity());
        }

        return Rootview;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                showAlertDialog();

                if(latitude!=null && longitude!=null && docFlag==1 ){
                    if(flag!=0){uploadFile();}
                    if(docFlag==1){uploadMultipart();uploaddocuments.setClickable(false);}
                createNewPlace(placeName.getText().toString(),addLine1.getText().toString(),absurd,city.getText().toString(),state.getText().toString(),bhk.getText().toString(),startingprice.getText().toString(),proprieterID,liftvalue,gardenvalue,swimmingpoolvalue,gymvalue,cafeteriavalue,housevalue,apartmentvalue,latitude.toString(),longitude.toString(),imageUrl);
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
                else{
                    showAlertDialog();
                }}
            }
            else {
                Toast.makeText(getActivity(), "Please enter all the Details correctly", Toast.LENGTH_SHORT).show();

            }

        }
        if(view.getId()==R.id.btnpic){
            pickImageFromGallery();
        }
         if(view.getId()==R.id.gpstv){
            if (haveNetworkConnection()) {
                if (isLocationEnabled()) {
                    b.dismiss();
                    callGps();
                } else {
                    showAlert();
                }

            }else{
                Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_SHORT).show();
            }
        }
        if(view.getId()== R.id.btndoc){
            showFileChooser();
        }
    }

    private void createNewPlace(String placeName, String addLine1, String addLine,String city,String state,String bhk,String startingPrice,String proprieterID,boolean lift,boolean garden,boolean swimmingpool,boolean gym,boolean cafeteria,boolean house,boolean apartment,String latitude,String longitude,String imageUrl){
        Place t = new Place(placeName,addLine1,addLine,city,state,bhk,startingPrice,proprieterID,lift,garden,swimmingpool,gym,cafeteria,house,apartment,latitude,longitude,imageUrl);
        sTasks.createDocument(t);
        reloadReplicationSettings();
        sTasks.startPushReplication();


    }
    public void pickImageFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 0);

    }


    private void reloadReplicationSettings() {
        try {
            this.sTasks.reloadReplicationSettings();
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

        dialogBuilder.setMessage("Enter Your Address\n");

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                docFlag=1;
            }
            // When an Image is picked
            else if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                //imgView.setImageBitmap(decodeBitmap(selectedImage));




                cursor.close();
                flag=1;

                File file=new File(mediaPath);
                nameImage=file.getName();
                setImageUrl();

            } else {
                Toast.makeText(getContext(), "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
    // Uploading Image/Video
    private void uploadFile() {
        progressDialog.show();

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());


        APIService getResponse = ApiClient.getClient().create(APIService.class);
        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getActivity().getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        setImageUrl();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }


    public void setImageUrl(){
        imageUrl="http://eventapp.000webhostapp.com/picUpload.php/ownerImages/"+nameImage;
    }


            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!(getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                    }
                }
                if (mGoogleApiClient !=null) {
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    mlocation=location;


                    if (location != null){
                        latitude=(mlocation.getLatitude());
                        longitude=(mlocation.getLongitude());

                        b.dismiss();


                    }
                    else{
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    }   }
            }

            @Override
            public void onConnectionSuspended(int i) {
            }

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
                if (connectionResult.hasResolution()) {
                    try {
                        // Start an Activity that tries to resolve the error
                        connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
                    } catch (IntentSender.SendIntentException e) {
                        // Log the error
                        e.printStackTrace();
                    }
                } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
                    Log.d("LogH", "Location services connection failed with code " + connectionResult.getErrorCode());
                }

            }
            @Override
            public void onLocationChanged(Location location) {
                //  currentLatitude = location.getLatitude();
                //  currentLongitude = location.getLongitude();
                mlocation=location;

                // Log.d("LogH", currentLatitude + " WORKS " + currentLongitude + "");
            }
///end gps methos

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_set_location, null);

        final EditText locality = (EditText) dialogView.findViewById(R.id.locality);
        final EditText city = (EditText) dialogView.findViewById(R.id.city);


        builder.setTitle("Allow us to locate you!!");

        builder.setView(dialogView);
        TextView gpstv = (TextView) dialogView.findViewById(R.id.gpstv);
        gpstv.setOnClickListener(this);



        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (haveNetworkConnection()) {
                    String localtyS = locality.getText().toString();
                    String cityS = city.getText().toString();
                    String address = localtyS + " " + cityS;
                    GeocodingLocation locationAddress = new GeocodingLocation();
                    String s = GeocodingLocation.getAddressFromLocation(address, getActivity());
                    Log.d("LogH", "from getLocation" + s);
                    dialog.dismiss();

                    if (s == null) {
                        Toast.makeText(getActivity(), "Sorry!!Coudn't get your location,Using gps", Toast.LENGTH_LONG).show();
                        callGps();
                    } else {

                        String[] valuesH = s.toString().split("\\s");
                        latitude = Double.parseDouble(valuesH[0]);
                        longitude = Double.parseDouble(valuesH[1]);
                        Log.d("LogH", "after values" + latitude + " " + longitude);

                    }

                }else{
                    Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface Dialog,int whichButton){
                Dialog.cancel();
            }
        });
            b=builder.create();
            b.show();

        }


    private boolean isLocationEnabled() {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }
    public void callGps(){

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);
        mGoogleApiClient.connect();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart() {
        //getting name for the image
        //String name = "Humra PDF";

        //getting the actual path of the image
        String path = FilePath.getPath(getActivity(), filePath);

        if (path == null) {

            Toast.makeText(getActivity(), "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf")//Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .addParameter("placename",placeName.getText().toString())
                        .startUpload(); //Starting the upload
                Toast.makeText(getActivity(),"Uploaded Successfully",Toast.LENGTH_SHORT).show();

            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    //method to show file chooser
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);

        // uploadMultipart();
    }

}