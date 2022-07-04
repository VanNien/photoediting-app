package com.example.appimage3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appimage3.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.color_item, viewGroup, false);

        return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder colorViewHolder, int position) {
        colorViewHolder.color_section.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        public CardView color_section;


        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            color_section =(CardView)itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }

    private List<Integer> genColorList() {
        List<Integer> colorList=new ArrayList<>();
        colorList.add(Color.parseColor("#332a2b"));
        colorList.add(Color.parseColor("#ffffb2"));
        colorList.add(Color.parseColor("#4286ff"));
        colorList.add(Color.parseColor("#469f70"));
        colorList.add(Color.parseColor("#ff5010"));
        colorList.add(Color.parseColor("#0ff1ce"));
        colorList.add(Color.parseColor("#ffffff"));
        colorList.add(Color.parseColor("#ffd7d5"));

        return colorList;
    }
}
