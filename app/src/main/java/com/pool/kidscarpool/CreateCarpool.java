package com.pool.kidscarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class CreateCarpool extends AppCompatActivity {
    private static final String TAG ="TAG" ;
    private static int id=0;
    private static final int PICK_CONTACT = 1000;
    EditText name, event, location, owner,member1,member2,member3;
    Button submit;

    FirebaseAuth firebaseAuth;
   // private ProgressDialog progressDialog;
     FirebaseFirestore firebaseFirestore;
     String user_id;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_carpool);
        Intent i = getIntent();
        name = findViewById(R.id.nameid);
        event = findViewById(R.id.eventid);
        member1=findViewById(R.id.member1edittext);
        member2=findViewById(R.id.member2edittext);
        member3=findViewById(R.id.member3edittext);
        location = findViewById(R.id.locationid);
        owner = findViewById(R.id.Ownername);
        submit = findViewById(R.id.savebtn);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String carpoolname = name.getText().toString();
                final String carpoolevent = event.getText().toString();
                final String carpoollocation = location.getText().toString();
                final String carpoolowner = owner.getText().toString();
                final String member_1=member1.getText().toString();
                final String member_2=member2.getText().toString();
                final String member_3=member3.getText().toString();
                id++;
                if (!TextUtils.isEmpty(carpoolname) && !TextUtils.isEmpty(carpoolevent) && !TextUtils.isEmpty(carpoollocation) && !TextUtils.isEmpty(carpoolowner)) {
                    storeCarpoolData(carpoolname, carpoolevent, carpoollocation, carpoolowner,member_1,member_2,member_3);
                    //id++;
                }
            }
        });
    }
    private void storeCarpoolData(String carpoolname, String carpoolevent, String carpoollocation, String carpoolowner,String member_1,String member_2,String member_3)

    {
         Map<String, String> cData = new HashMap<>();
      //  cData.put("id",String.valueOf(id));
        cData.put("Carpool", carpoolname);
        cData.put("event", carpoolevent);
        cData.put("location", carpoollocation);
        cData.put("owner", carpoolowner);
        cData.put("member_1",member_1);
        cData.put("member_2",member_2);
        cData.put("member_3",member_3);
        //firebaseFirestore.collection("Users").document(user_id).update(String.valueOf(cData),true).update(String.valueOf(cData),true)
        firebaseFirestore.collection(user_id).document(String.valueOf(id)).set(cData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //progressDialog.dismiss();
                    Toast.makeText(CreateCarpool.this, "Carpool Data is Stored Successfully", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(CreateCarpool.this,DisplayCarpool.class);
                    startActivity(i);
                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(CreateCarpool.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                }

            }

        });
    }


    }