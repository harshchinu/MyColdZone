package com.example.mycoldzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Common.Common;
import Model.User;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnSignUp;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button)findViewById(R.id.btnsignIn);
        btnSignUp = (Button)findViewById(R.id.btnsignUp);


        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

        //Paper init

        Paper.init(this);



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(MainActivity.this,signIn.class);
                startActivity(signIn);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this,signUp.class);
                startActivity(signUp);
            }
        });

        //Check Remember

        String user = Paper.book().read(Common.User_key);
        String pwd = Paper.book().read(Common.PWD_key);

        if(user !=null && pwd !=null){
            if(!user.isEmpty() && !pwd.isEmpty())
                login(user,pwd);
        }
    }

    private void login(final String phone, final String pwd) {
        // Just again login code here from signin.class

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");



        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please Waiting");
        mDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //checking whether user exising in Databse or not

                if(dataSnapshot.child(phone).exists()) {
                    //get User information
                    mDialog.dismiss();
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if(user.getPassword().equals(pwd)) {
                        {
                            Intent homeintent = new Intent(MainActivity.this, Home.class);
                            Common.currentUSer = user;
                            startActivity(homeintent);
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "User not exists", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
