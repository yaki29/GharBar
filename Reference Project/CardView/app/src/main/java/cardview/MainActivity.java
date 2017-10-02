package cardview;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();


    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,
                R.drawable.house,};

        Album a = new Album("434-A lajpat nagar Alwar", 13000, covers[0]);
        albumList.add(a);

        a = new Album("14 Malviya Nagar Jaipur", 8000, covers[1]);
        albumList.add(a);

        a = new Album("C Scheme Jaipur", 11000, covers[2]);
        albumList.add(a);

        a = new Album("Tonk Road Jaipur", 12000, covers[3]);
        albumList.add(a);

        a = new Album("60-A Mahavir Nagar", 14000, covers[4]);
        albumList.add(a);

        a = new Album("434-A lajpat nagar Alwar", 13000, covers[0]);
        albumList.add(a);

        a = new Album("14 Malviya Nagar Jaipur", 8000, covers[1]);
        albumList.add(a);

        a = new Album("C Scheme Jaipur", 11000, covers[2]);
        albumList.add(a);

        a = new Album("Tonk Road Jaipur", 12000, covers[3]);
        albumList.add(a);

        a = new Album("60-A Mahavir Nagar", 14000, covers[4]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
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
}
