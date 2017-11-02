package com.example.user.gharbar.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudant.sync.documentstore.DocumentStoreException;
import com.example.user.gharbar.Activities.MapActivity;
import com.example.user.gharbar.Adapters.AlbumsAdapter;
import com.example.user.gharbar.Models.Place;
import com.example.user.gharbar.R;
import com.example.user.gharbar.Utilities.PlaceModel;
import com.google.android.gms.identity.intents.Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ViewPlacesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AlbumsAdapter.ListItemClickListener {

    private RecyclerView recyclerView;
    View view;
    private int a = 0;
    private AlbumsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Place> tasks;
    private double latitude;
    ArrayList<Place> filtertask;
    private double longitude;
    PlaceModel stask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_places, container, false);
        prepareAlbums();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        view = v.findViewById(R.id.empty_view);
        view.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        // Protect creation of static variable.
        if (stask == null) {
            // Model needs to stay in existence for lifetime of app.
            this.stask = new PlaceModel(getActivity());
        }

        this.stask.setReplicationListener(this);
        //stask.startPullReplication();
        // Load the tasks from the model
        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.green, R.color.red);
        swipeRefreshLayout.setOnRefreshListener(this);
        //   this.reloadTasksFromModel();


//        try {
//            place= (ArrayList<Place>) stask.allTasks();
//
//        } catch (DocumentStoreException e) {
//            e.printStackTrace();
//        }
//        stask.startPullReplication();

//        if(place!=null){
//
//            recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
//
//                       adapter = new AlbumsAdapter(getContext(), place);
//
//            recyclerView.setLayoutManager(mLayoutManager);
//            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
//            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(adapter);
//
//
//        }
//        else {
//            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
//        }

        return v;


    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("Log", "run");
                view.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(true);
                reloadTasksFromModel();

            }
        }, 5000);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) throws IOException {
        Log.v("Item No", "" + clickedItemIndex);
//        Log.v("PlaceModel",""+tasks.get(clickedItemIndex).getPlaceName());
        Place place = tasks.get(clickedItemIndex);
        if(filtertask!=null){
            place = filtertask.get(clickedItemIndex);
        }else{
             place = tasks.get(clickedItemIndex);
        }

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            android.location.Address location = geocoder.getFromLocationName(place.getAddLine1()+place.getAddLine2()+place.getCity(), 1).get(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
           // Log.v("stringyash", "" + location.getLatitude()+"  adapter"+place.getLatitude());
            Log.v("stringyashlongitude", "" + location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getActivity().getBaseContext(), MapActivity.class);
        intent.putExtra("Proprietor", place.getProprieterID());
        //intent.putExtra("Tenant",place.getT);
        intent.putExtra("Name", place.getPlaceName());
        intent.putExtra("Add", place.getAddLine1());
        intent.putExtra("Price", place.getStartingPrice());
        intent.putExtra("Gym", place.isGym());
        intent.putExtra("Swimming Pool", place.isSwimmingpool());
        intent.putExtra("Garden", place.isGarden());
        intent.putExtra("Lift", place.isLift());
        intent.putExtra("Cafeteria", place.isCafeteria());
        intent.putExtra("Lat", latitude);
        intent.putExtra("Long", longitude);     //For my future reference
//        intent.putExtra("Lat",Double.parseDouble(place.getLatitude()));
//        intent.putExtra("Long",Double.parseDouble(place.getLongitude()));
        getActivity().startActivity(intent);

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.gharbar,
                R.drawable.gharbar, R.drawable.gharbar,
                R.drawable.gharbar,
                R.drawable.gharbar,


        };
    }

    private void reloadTasksFromModel() {
        try {
            //recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
            stask.startPullReplication();
            tasks = (ArrayList<Place>) this.stask.allTasks();
            if (tasks != null) {
                this.adapter = new AlbumsAdapter(this.getActivity(), tasks, this);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
                recyclerView.setLayoutManager(mLayoutManager);
                //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(this.adapter);
                Log.v("Log123", "" + adapter.getItemCount());
                if (adapter.getItemCount() == 0) {
                    view.setVisibility(View.VISIBLE);
                }


            } else {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        } catch (DocumentStoreException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDestroy() {
        Log.d("LOG_TAG", "onDestroy()");

        recyclerView.setAdapter(null);
        // Clear our reference as listener.
        this.stask.setReplicationListener(null);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.stask.setReplicationListener(this);

        //stask.startPullReplication();
        // Load the tasks from the model
        this.reloadTasksFromModel();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.filter, menu);


    }

    //for action button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.location:
            /*   MenuItem menuItem = item;
                menuItem.setActionView(R.layout.progressbar);  */
                showAlertDialog();
                break;
            // action with ID action_settings was selected

            default:
                break;
        }

        return true;
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filterdialog, null);
        final EditText city = (EditText) dialogView.findViewById(R.id.filtercity);
        final EditText to = (EditText) dialogView.findViewById(R.id.filterto);
        final EditText from = (EditText) dialogView.findViewById(R.id.filterfrom);
        builder.setTitle("Filter");
        builder.setView(dialogView);


        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            /*String City=city.getText().toString();
            int too=Integer.parseInt(to.getText().toString());
            int fromm=Integer.parseInt(from.getText().toString());*/
            public void onClick(DialogInterface dialog, int whichButton) {
                if (haveNetworkConnection()) {
                    reloadTasksFromModelFilter(city.getText().toString(), Integer.parseInt(to.getText().toString()), Integer.parseInt(from.getText().toString()));

                } else {
                    Toast.makeText(getActivity(), "No Internet connection", Toast.LENGTH_SHORT).show();
                }
            }

        });

        AlertDialog b = builder.create();
        b.show();


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

    private void reloadTasksFromModelFilter(String city, int to, int from) {
        try {
            filtertask = new ArrayList<Place>();
            Log.v("Log123", "" + adapter.getItemCount());
           /* if(adapter.getItemCount()==0)
            {
                view.setVisibility(View.VISIBLE);
            }*/
            //recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
            stask.startPullReplication();
            tasks = (ArrayList<Place>) this.stask.allTasks();
            if (tasks != null) {
                if (!city.equals(null) && to != 0 && from != 0) {
                    for (int i = 0; i < tasks.size(); i++) {
                        if ((tasks.get(i).getCity().toLowerCase().equals(city.toLowerCase())) && Integer.parseInt(tasks.get(i).getStartingPrice()) <= from && Integer.parseInt(tasks.get(i).getStartingPrice()) >= to)
                            filtertask.add(tasks.get(i));
                    }
                } else if (!city.equals(null) && from != 0) {
                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).getCity().toLowerCase().equals(city.toLowerCase()) && Integer.parseInt(tasks.get(i).getStartingPrice()) <= from)
                            filtertask.add(tasks.get(i));
                    }

                }
                //else if(city!=null && from==0)
                else if (!city.equals(null) && from == 0) {
                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).getCity().toLowerCase().equals(city.toLowerCase()))

                            filtertask.add(tasks.get(i));
                    }
                } else {
                    Toast.makeText(getContext(), "not filtered", Toast.LENGTH_SHORT).show();
                    filtertask = tasks;
                }


                this.adapter = new AlbumsAdapter(this.getActivity(), filtertask, this);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
                recyclerView.setLayoutManager(mLayoutManager);
                //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(this.adapter);
                Log.v("Log123", "" + adapter.getItemCount());
                if (adapter.getItemCount() == 0) {
                    view.setVisibility(View.VISIBLE);
                }
//                tasks = null;
//                filtertask = tasks;


            } else {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        } catch (DocumentStoreException e) {
            throw new RuntimeException(e);
        }

    }

}

