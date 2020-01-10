package com.example.mycoldzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycoldzone.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import Common.Common;
import Model.Food;
import Model.Order;
import Model.SubmittedOrders;
import Model.User;

public class Orders extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<SubmittedOrders, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Orders");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUSer.getPhone());
    }

    private void loadOrders(String phone) {

        FirebaseRecyclerOptions<SubmittedOrders> options;

        Query query = requests.orderByChild("phone").equalTo(phone);

        options = new FirebaseRecyclerOptions.Builder<SubmittedOrders>()
                .setQuery(query,SubmittedOrders.class).build();


            adapter = new FirebaseRecyclerAdapter<SubmittedOrders,OrderViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull SubmittedOrders submittedOrders) {
                        orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                        orderViewHolder.txtPrice.setText(submittedOrders.getTotal());
            }
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new OrderViewHolder(view);
            }

        };



        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


}
