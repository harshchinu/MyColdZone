package com.example.mycoldzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycoldzone.Database.Database;
import com.example.mycoldzone.Helper.RecycleItemTouchHelper;
import com.example.mycoldzone.Interface.RecyclerItemTouchHelperListener;
import com.example.mycoldzone.ViewHolder.CartAdapter;
import com.example.mycoldzone.ViewHolder.CartViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;
    RelativeLayout relativeLayout;

    Date currentTime;
    String currentDate;

    String orderdetails = "";
    SharedPreferences pref;


    String finalstring = "";
    String heading = " ";


    public TextView txtTotalPrice;
    int token = 0;
    FButton btnOrder;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        currentTime = Calendar.getInstance().getTime();
        currentDate = currentTime.toString().substring(8, 10);
        pref = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);


        //Firebase
        //token=0;
        database = FirebaseDatabase.getInstance();
        request = database.getReference("Orders");


        //paper init
        // Paper.init(this);

        //Init

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        relativeLayout = (RelativeLayout) findViewById(R.id.rootlayout);

        //swipe to delte

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecycleItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnOrder = (FButton) findViewById(R.id.btnPlaceOrder);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
            String formattedDate = df.format(c);

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final SubmittedOrders submittedOrders = new SubmittedOrders(
                        txtTotalPrice.getText().toString(),
                        formattedDate,
                        Common.currentUSer.getPhone(),
                        cart
                );


                String lastdate = pref.getString("LastDate", "abc");
                if (lastdate.equals("abc")) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("LastDate", currentDate);
                    editor.commit();
                }


                if (!lastdate.equals(currentDate)) {
                    token = 0;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("TokenNumber", token);
                    editor.putString("LastDate", currentDate);
                    editor.commit();
                }

                token = pref.getInt("TokenNumber", 0);
                token++;
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("TokenNumber", token);
                editor.commit();


                request.child(String.valueOf(token))
                        .setValue(submittedOrders);

                orderdetails = submittedOrders.getFood().toString();


                //<PRINTER repeat='2'>

                heading = "<BIG><BOLD><CENTER>MY COLD ZONE<BR>"
                        + "<LINE>" + "<BIG> TOKEN NO: <BR>" + "<CENTER>" + "<BIG>" + token + "<BR><LINE>"
                        + "<BOLD> Name            Quantity    Price    Subtotal <BR> \n";

                orderprint();


                    /*Locale locale = new Locale("en","IN");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);*/



                    heading+="<BR><LINE>"+"                           "+"<BOLD>Total = "+"Rs "+txtTotalPrice.getText().toString()+".00";

                    Intent intent = new Intent("pe.diegoveloper.printing");
                    //intent.setAction(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT,heading);
                    startActivity(intent);


                    new Database(getBaseContext()).cleanCart();

                Toast.makeText(Cart.this, "Thank you for your order", Toast.LENGTH_SHORT).show();
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

    private void orderprint() {

        int i;
        String[] a = orderdetails.split(",");
        for (i = 0; i < a.length; i++) {
            a[i] += Character.toString('\n');
        }
        String[] str = new String[6];


        for (String s : a) {

            int len = 20;
            str[0] = " ";
            int space=0;
            str[5] = " ";
            for (i = 28; !Character.toString(s.charAt(i)).equals("Q"); i++)
                str[0] += Character.toString(s.charAt(i));


            str[1] = Character.toString(s.charAt(i + 9));
            for (i = i + 10; !Character.toString(s.charAt(i)).equals("P"); i++)
                str[1] += Character.toString(s.charAt(i));


            str[2] = Character.toString(s.charAt(i + 5));
            for (i = i + 6; Character.isDigit(s.charAt(i)); i++)
                str[2] += Character.toString(s.charAt(i));


            str[3] = Integer.toString(Integer.parseInt(str[1]) * Integer.parseInt(str[2]));

            space += len - str[0].length();

            /*StringBuffer outputBuffer = new StringBuffer(str[4].length());
            for (int j = 0; j < str[4].length(); j++) {
                if (j > 0)
                    outputBuffer.append(" ");
            }
            str[5]=outputBuffer.toString();*/

            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j <space; j++) {
                stringBuilder.append(" ");
            }
            str[5] = stringBuilder.toString();

            finalstring="<LEFT>"+str[0]+str[5]+str[1]+"          "+str[2]+"       "+str[3]+"<BR>";

            heading+=finalstring;
        }



        //System.out.println(str[0]+" "+str[1]+" "+str[2]+" "+str[3]);

               //finalstring="<LEFT>"+str[0]+str[5]+str[1]+str[2]+str[3]+"<BR>";

                //

              // System.out.println(finalstring);


    }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for(Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        //Locale locale = new Locale("en","IN");
        //NumberFormat fmt = NumberFormat.getInstance(new Locale("en","IN"));
//txtTotalPrice.setText(fmt.format(total));

        txtTotalPrice.setText(": "+Integer.toString(total));
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
           // Locale locale = new Locale("en","IN");
          //  NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
           // txtTotalPrice.setText(fmt.format(total));
            txtTotalPrice.setText(": "+Integer.toString(total));



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
                    //Locale locale = new Locale("en","IN");
                   // NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    //txtTotalPrice.setText(fmt.format(total));

                    txtTotalPrice.setText(": "+Integer.toString(total));
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}
