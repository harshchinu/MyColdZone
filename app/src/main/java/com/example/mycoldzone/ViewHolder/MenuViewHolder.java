package com.example.mycoldzone.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoldzone.Interface.itemClickListener;
import com.example.mycoldzone.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName;
    public ImageView imageView;

    private itemClickListener ItemClickListener;

    public MenuViewHolder(View itemView){
        super(itemView);


        txtMenuName=(TextView)itemView.findViewById(R.id.menu_name);
        imageView = (ImageView)itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    public itemClickListener getItemClickListener() {
        return ItemClickListener;
    }

    public void setItemClickListener(itemClickListener itemClickListener) {
        ItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        ItemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
