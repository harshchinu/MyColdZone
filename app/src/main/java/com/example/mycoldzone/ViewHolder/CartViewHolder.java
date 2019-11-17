package com.example.mycoldzone.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mycoldzone.Interface.itemClickListener;
import com.example.mycoldzone.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_cart_name,txt_price;
    public ElegantNumberButton btnquantity;

    private itemClickListener ItemClickListener;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView)itemView.findViewById(R.id.cart_item_Price);
        btnquantity=(ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
        view_background=(RelativeLayout)itemView.findViewById(R.id.view_background);
        view_foreground=(LinearLayout)itemView.findViewById(R.id.view_foreground);

    }

    @Override
    public void onClick(View v) {

    }
}