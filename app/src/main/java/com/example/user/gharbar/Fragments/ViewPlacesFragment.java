package com.example.user.gharbar.Fragments;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.user.gharbar.Adapters.AlbumsAdapter;
import com.example.user.gharbar.Models.Place;
import com.example.user.gharbar.R;
import com.example.user.gharbar.Utilities.PlaceModel;

import java.util.ArrayList;

public class ViewPlacesFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    ArrayList<Place> place;
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
        // Protect creation of static variable.
        if (stask == null) {
            // Model needs to stay in existence for lifetime of app.
            this.stask = new PlaceModel(getActivity());
        }


        try {
            place= (ArrayList<Place>) stask.allTasks();
            Log.d("dhvkjsdv",place.get(0).getCity());
        } catch (DocumentStoreException e) {
            e.printStackTrace();
        }

        if(place!=null){

            recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

                       adapter = new AlbumsAdapter(getContext(), place);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);



        }
        else {
            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
        }

        return v;


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
    }}

