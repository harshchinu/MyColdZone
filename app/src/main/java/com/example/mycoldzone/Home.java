package com.example.mycoldzone;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;

        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.navigation.NavController;
        import androidx.navigation.Navigation;
        import androidx.navigation.ui.AppBarConfiguration;
        import androidx.navigation.ui.NavigationUI;
        import androidx.recyclerview.widget.GridLayoutManager;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;


        import com.andremion.counterfab.CounterFab;
        import com.example.mycoldzone.Database.Database;
        import com.example.mycoldzone.Interface.itemClickListener;
        import com.example.mycoldzone.ViewHolder.MenuViewHolder;
        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.navigation.NavigationView;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import com.squareup.picasso.Picasso;

        import Common.Common;
        import Model.Category;
        import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;
    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullname;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    CounterFab fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);


        //firebase init
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        //paper init
      //  Paper.init(this);

        fab =(CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        fab.setCount(new Database(this).getCountCart());
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_menu, R.id.nav_cart, R.id.nav_orders,
                R.id.nav_log_out)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        //setName for User
        View headerView = navigationView.getHeaderView(0);
       // txtFullname = (TextView) headerView.findViewById(R.id.txtFullName);
    //    txtFullname.setText(Common.currentUSer.getName());

        //Load menu
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_home);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getCountCart());
    }

    private void loadMenu() {

        FirebaseRecyclerOptions<Category> options;

        options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category, Category.class).build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int position, @NonNull Category category) {

                /*Picasso.with(Home.this).load(category.getImage()).fit()
                        .into(menuViewHolder.imageView);*/

                Picasso.with(getBaseContext()).load(category.getImage())
                        .into(menuViewHolder.imageView);
                menuViewHolder.txtMenuName.setText(category.getName());
                final Category clickItem = category;
                menuViewHolder.setItemClickListener(new itemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Getting Catageory id and Setting it to new Activity
                        Intent foodlist = new Intent(Home.this, FoodList.class);
                        //Because category id is which we gonna use to as menu id in foodlist
                        foodlist.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodlist);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout, parent, false);
                return new MenuViewHolder(view);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recycler_menu.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recycler_menu.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //handle navigate click here
        int id= menuItem.getItemId();
        if(id==R.id.nav_menu){
        }else if(id==R.id.nav_cart){
            Intent cartIntent = new Intent(Home.this,Cart.class);
            startActivity(cartIntent);
        }else if(id==R.id.nav_orders){
            Intent orderIntent = new Intent(Home.this,Orders.class);
            startActivity(orderIntent);
        }else if (id == R.id.nav_log_out){
            //delete the store data from paper db user id and password
            //Paper.book().destroy();

            Paper.book().delete(Common.User_key);
            Paper.book().delete(Common.PWD_key);
            Intent SignIn = new Intent(Home.this, signIn.class);
            startActivity(SignIn);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}

