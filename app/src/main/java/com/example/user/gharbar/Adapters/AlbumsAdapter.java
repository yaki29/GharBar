package com.example.user.gharbar.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.gharbar.Models.Place;
import com.example.user.gharbar.R;

import java.util.ArrayList;


/**
 * Created by Pablo Shakun 2/10/2019.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    String validation;
    private ArrayList<Place> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,verified;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            verified=(TextView)view.findViewById(R.id.verified);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }


    public AlbumsAdapter(Context mContext, ArrayList<Place> albumList) {
        Log.v("SIZE",albumList.size()+"");
        if (mContext == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        if (albumList == null) {
            throw new IllegalArgumentException("List of tasks must not be null.");
        }
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Place album = albumList.get(position);
        holder.title.setText(album.getPlaceName());
        if(album.getValidation()==false){
            Log.v("hello","KO");

           holder.verified.setText("Verification Pending");
            holder.verified.setTextColor(Color.parseColor("#FFF40404"));
        }
        else{
            holder.verified.setText("Verified");
            holder.verified.setTextColor(Color.parseColor("#FF14F404"));
        }
        holder.count.setText("Expected Rent ::"+album.getStartingPrice()+"INR");
        Glide.with(mContext).load(R.drawable.gharbar).into(holder.thumbnail);


    }
    @Override
    public int getItemCount() {
        return albumList.size();
    }
}