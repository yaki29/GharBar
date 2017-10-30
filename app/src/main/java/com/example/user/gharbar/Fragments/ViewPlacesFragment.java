package com.example.user.gharbar.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cloudant.sync.documentstore.DocumentStoreException;
import com.example.user.gharbar.Activities.TenantActivity;
import com.example.user.gharbar.Adapters.AlbumsAdapter;
import com.example.user.gharbar.Map_Activity;
import com.example.user.gharbar.Models.Place;
import com.example.user.gharbar.R;
import com.example.user.gharbar.Utilities.PlaceModel;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ViewPlacesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AlbumsAdapter.ListItemClickListener {

    private RecyclerView recyclerView;
    View view;
    private int a=0;
    private AlbumsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Place> tasks;
    PlaceModel stask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_view_places, container, false);
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

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.green,R.color.red);
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
                Log.v("Log","run");
                view.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(true);
                reloadTasksFromModel();

            }
        },5000);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.v("Item No",""+clickedItemIndex);
//        Log.v("PlaceModel",""+tasks.get(clickedItemIndex).getPlaceName());
        Place place = tasks.get(clickedItemIndex);

        Intent intent = new Intent(getActivity().getBaseContext(),Map_Activity.class);
        intent.putExtra("Name",place.getPlaceName());
        intent.putExtra("Add",place.getAddLine1());
        intent.putExtra("Price",place.getStartingPrice());
        intent.putExtra("Gym",place.isGym());
        intent.putExtra("Swimming Pool",place.isSwimmingpool());
        intent.putExtra("Garden",place.isGarden());
        intent.putExtra("Lift",place.isLift());
        intent.putExtra("Cafeteria",place.isCafeteria());
        intent.putExtra("Lat",27.552494);
        intent.putExtra("Long",76.631267);
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
            if(tasks!=null) {
                this.adapter = new AlbumsAdapter(this.getActivity(), tasks,this);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
                recyclerView.setLayoutManager(mLayoutManager);
                //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(this.adapter);
                Log.v("Log123",""+adapter.getItemCount());
                if(adapter.getItemCount()==0)
                {
                    view.setVisibility(View.VISIBLE);
                }



            }
            else{
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
        super.onDestroy();
        recyclerView.setAdapter(null);
        // Clear our reference as listener.
        this.stask.setReplicationListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.stask.setReplicationListener(this);
        //stask.startPullReplication();
        // Load the tasks from the model
        this.reloadTasksFromModel();
    }
}

