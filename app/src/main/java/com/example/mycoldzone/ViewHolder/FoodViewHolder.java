package com.example.mycoldzone.ViewHolder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoldzone.Interface.itemClickListener;
import com.example.mycoldzone.R;

public class FoodViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView food_name;
    public ImageView food_image;



    private itemClickListener ItemClickListener;

    public void setItemClickListener(itemClickListener itemClickListener) {
        ItemClickListener = itemClickListener;
    }

    public FoodViewHolder (View itemView) {
        super(itemView);

        food_name=(TextView)itemView.findViewById(R.id.food_name);
        food_image = (ImageView)itemView.findViewById(R.id.food_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ItemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
