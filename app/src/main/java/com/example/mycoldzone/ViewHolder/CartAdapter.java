package com.example.mycoldzone.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mycoldzone.Cart;
import com.example.mycoldzone.Database.Database;
import com.example.mycoldzone.Interface.itemClickListener;
import com.example.mycoldzone.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.Order;



public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {

       /* TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getQuantity(), Color.RED);

        holder.img_cart_count.setImageDrawable(drawable);
*/
       holder.btnquantity.setNumber(listData.get(position).getQuantity());

       holder.btnquantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
           @Override
           public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
               Order order=listData.get(position);
               order.setQuantity(String.valueOf(newValue));
               new Database(cart).updateCart(order);


               int total = 0;
               List<Order> orders = new Database(cart).getCarts();
               for(Order item:orders)
                   total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(item.getQuantity()));
              /* Locale locale = new Locale("en","IN");
               NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

               cart.txtTotalPrice.setText(fmt.format(total));*/
               cart.txtTotalPrice.setText(": "+Integer.toString(total));
           }
       });
       int Quantity = (Integer.parseInt(listData.get(position).getQuantity()));
       System.out.println(Quantity);

        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        System.out.println(price);
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position){ return listData.get(position);}

    public void remoteItem(int position){ listData.remove(position); notifyItemRemoved(position);}

    public void restoreItem(Order item,int position){ listData.add(position,item); notifyItemInserted(position);}



}
