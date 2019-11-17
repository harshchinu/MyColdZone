package com.example.mycoldzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycoldzone.Database.Database;
import com.example.mycoldzone.Helper.RecycleItemTouchHelper;
import com.example.mycoldzone.Interface.RecyclerItemTouchHelperListener;
import com.example.mycoldzone.ViewHolder.CartAdapter;
import com.example.mycoldzone.ViewHolder.CartViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Common.Common;
import Model.Order;
import Model.SubmittedOrders;
import info.hoang8f.widget.FButton;
import io.paperdb.Paper;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;
    RelativeLayout relativeLayout;

    public  TextView txtTotalPrice;

    FButton btnOrder;
    int token=0;
    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase

        database = FirebaseDatabase.getInstance();
        request= database.getReference("Requests");


        //paper init
       // Paper.init(this);

        //Init

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        relativeLayout=(RelativeLayout)findViewById(R.id.rootlayout);

        //swipte to delte

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecycleItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnOrder=(FButton)findViewById(R.id.btnPlaceOrder);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("Token",token);
                showAlertDialog();

            }
        });
        loadListFood();
    }

        private void showAlertDialog() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
            alertDialog.setTitle("Confirmation");
            alertDialog.setMessage("Click when ok to print");

            /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );

            AlertDialog.Builder builder = alertDialog.setView(lp);*/

            alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-YY");
                String formattedDate = df.format(c);

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final SubmittedOrders submittedOrders = new SubmittedOrders(
                            txtTotalPrice.getText().toString(),
                            formattedDate,
                            Common.currentUSer.getPhone(),
                            cart
                    );


                    //reading token from  Paper db

                    Paper.book().read("Token");
                    token=token++;
                    request.child(String.valueOf(token))
                            .setValue(submittedOrders);

                    new Database(getBaseContext()).cleanCart();

                    Toast.makeText(Cart.this,"Thank you for your order",Toast.LENGTH_SHORT).show();
                    finish();


                }
            });


            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();

        }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for(Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartViewHolder){
            String name = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());

            final int DeleteIndex = viewHolder.getAdapterPosition();

            adapter.remoteItem(DeleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId());
            //Common.currentUSer.getPhone());

            //update updating total
            int total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts();
            for(Order item:orders)
                total+=(Integer.parseInt(item.getPrice()))*(Integer.parseInt(item.getQuantity()));
            Locale locale = new Locale("en","IN");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));



            //make snackbar

            Snackbar snackbar = Snackbar.make(relativeLayout,name+"removed from cart!",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem,DeleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    //updating the cart

                    int total = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts();
                    for(Order item:orders)
                        total+=(Integer.parseInt(item.getPrice()))*(Integer.parseInt(item.getQuantity()));
                    Locale locale = new Locale("en","IN");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    txtTotalPrice.setText(fmt.format(total));
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}
