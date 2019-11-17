package com.example.mycoldzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import Common.Common;
import Model.User;
import io.paperdb.Paper;

public class signIn extends AppCompatActivity {


    EditText edtPhone,edtPassword;
    Button btnSignIn;

    private com.rey.material.widget.CheckBox ckbremember;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone=(MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn=(Button)findViewById(R.id.btnsignIn);
        ckbremember=(com.rey.material.widget.CheckBox)findViewById(R.id.ckbRemember);

        //init paperdb
         Paper.init(this);

        //Firebase init
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //save user & password
                if(ckbremember.isChecked()){
                    Paper.book().write(Common.User_key,edtPhone.getText().toString());
                    Paper.book().write(Common.PWD_key,edtPassword.getText().toString());
                }


                final ProgressDialog mDialog = new ProgressDialog(signIn.this);
                mDialog.setMessage("Please Waiting");
                mDialog.show();




                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //checking whether user exising in Databse or not

                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            //get User information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setPhone(edtPhone.getText().toString());
                            if(user.getPassword().equals(edtPassword.getText().toString())) {
                                {
                                    Intent homeintent = new Intent(signIn.this,Home.class);
                                    Common.currentUSer=user;
                                    startActivity(homeintent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(signIn.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(signIn.this, "User not exists", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
