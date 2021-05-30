package com.pool.kidscarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class UserProfile extends AppCompatActivity {
    TextView nameshow,emailidshow,addressshow,kidnameshow;

    ImageView profilepic;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profilepic=findViewById(R.id.user_image_show);
        nameshow=findViewById(R.id.user_name_show);
        emailidshow=findViewById(R.id.user_email_show);
        addressshow=findViewById(R.id.user_address_show);
        kidnameshow=findViewById(R.id.user_child_show);

    }

    @Override
    public void onStart() {
        super.onStart();
        user= FirebaseAuth.getInstance().getCurrentUser();
        String currentid=user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        reference=firestore.collection("Users").document(currentid);
       reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    String nameResult=task.getResult().getString("username");
                    String emailResult=task.getResult().getString("userphone");
                    String addressResult=task.getResult().getString("useraddress");
                    String imageResult=task.getResult().getString("userimage");
                    String kidnameResult=task.getResult().getString("userchildname");
                //   Picasso.get().load(imageResult).into(profilepic);
                    Glide.with(getApplicationContext())
                            .load(imageResult)
                            .into(profilepic);

                   nameshow.setText(nameResult);
                   emailidshow.setText(emailResult);
                   addressshow.setText(addressResult);
                   kidnameshow.setText(kidnameResult);
                }
                else{
                    Intent i=new Intent(UserProfile.this,ProfileCreation.class);
                    startActivity(i);
                }
            }
        });

    }
}