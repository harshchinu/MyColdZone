package com.example.mycoldzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.mycoldzone.Interface.itemClickListener;
import com.example.mycoldzone.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import Model.Food;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    String categoryId="";

    CounterFab fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");
        recyclerView = (RecyclerView)findViewById(R.id.recycle_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);


        //recyclerView.setLayoutManager(layoutManager);


        //getting intent value here




        if(getIntent() != null){
            categoryId=getIntent().getStringExtra("CategoryId");

        }
       if(!categoryId.isEmpty() && categoryId !=null){
            loadListFood(categoryId);

        }

    }

    private void loadListFood(String categoryId) {

       Query query = foodList.orderByChild("MenuId").equalTo(categoryId);


       /* FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodList.orderByChild("MenuId").equalTo(categoryId), Food.class)
                .build();*/

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(query,Food.class).build();



        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Food food) {
                 Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodViewHolder.food_image);

                foodViewHolder.food_name.setText(food.getName());
                final Food clickItem = food;


                foodViewHolder.setItemClickListener(new itemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent foodDetail = new Intent(FoodList.this,FoodDetails.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });


            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(view);

            }
        };


      GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);


        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }




}

