package com.example.appimage3.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.appimage3.Interface.FiltersListFragmentListener;
import com.example.appimage3.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailAdapter  extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {


    private List<ThumbnailItem> thumbnailItems;
    private FiltersListFragmentListener listener;
    private Context context;
    private int selectedIndex=0;

    public ThumbnailAdapter(List<ThumbnailItem> thumbnailItems, FiltersListFragmentListener listener, Context context) {
        this.thumbnailItems = thumbnailItems;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.thumbnail_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        ThumbnailItem thumbnailItem=thumbnailItems.get(position);

        myViewHolder.thumbnail.setImageBitmap(thumbnailItem.image);
        myViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFilterSeleted(thumbnailItem.filter);
                selectedIndex= myViewHolder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        myViewHolder.filtername.setText(thumbnailItem.filterName);

        if (selectedIndex==position)
            myViewHolder.filtername.setTextColor(ContextCompat.getColor(context,R.color.selected_filter));
        else  myViewHolder.filtername.setTextColor(ContextCompat.getColor(context,R.color.normal_filter));
    }

    @Override
    public int getItemCount() {
        return thumbnailItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView filtername;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail =(ImageView)itemView.findViewById(R.id.thumbnail);
            filtername=(TextView)itemView.findViewById(R.id.filter_name);
        }


    }
}
