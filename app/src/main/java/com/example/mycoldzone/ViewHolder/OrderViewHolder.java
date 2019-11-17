package com.example.mycoldzone.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoldzone.Interface.itemClickListener;
import com.example.mycoldzone.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId,txtPrice;

    private itemClickListener ItemClickListener;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderId=(TextView)itemView.findViewById(R.id.order_id);
        txtPrice=(TextView)itemView.findViewById(R.id.order_price);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(itemClickListener itemClickListener) {
        ItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        ItemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
