package com.pool.kidscarpool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateEvent extends AppCompatActivity {
    private static int eventid=10;
    EditText ename, eevent, elocation, eowner,emember1,emember2,emember3;
    Button schedule,savee;
    FirebaseAuth firebaseAuth;
    // private ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ename=findViewById(R.id.nameide);
        eevent=findViewById(R.id.eventide);
        elocation=findViewById(R.id.locationide);
        eowner=findViewById(R.id.Owneride);
        emember1=findViewById(R.id.member1edittexte);
        emember2=findViewById(R.id.member2edittexte);
        emember3=findViewById(R.id.member3edittexte);
        schedule=findViewById(R.id.schedulebutton);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        savee=findViewById(R.id.savebtne);
        savee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ecarpoolname = ename.getText().toString();
                final String ecarpoolevent = eevent.getText().toString();
                final String ecarpoollocation = elocation.getText().toString();
                final String ecarpoolowner = eowner.getText().toString();
                final String emember_1=emember1.getText().toString();
                final String emember_2=emember2.getText().toString();
                final String emember_3=emember3.getText().toString();
                eventid++;
                if (!TextUtils.isEmpty(ecarpoolname) && !TextUtils.isEmpty(ecarpoolevent) && !TextUtils.isEmpty(ecarpoollocation) && !TextUtils.isEmpty(ecarpoolowner) && !TextUtils.isEmpty(emember_1) && !TextUtils.isEmpty(emember_2) && !TextUtils.isEmpty(emember_3)) {
                    storeEventData(ecarpoolname, ecarpoolevent, ecarpoollocation, ecarpoolowner,emember_1,emember_2,emember_3);
                //    eventid++;
                }
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eowner.getText().toString().isEmpty() && !emember1.getText().toString().isEmpty() && !emember2.getText().toString().isEmpty() && !emember3.getText().toString().isEmpty())
                {
                    Intent intent=new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE,ename.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION,eevent.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION,elocation.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY,true);
                    intent.putExtra(Intent.EXTRA_EMAIL,eowner.getText().toString());
                    intent.putExtra(Intent.EXTRA_EMAIL,emember1.getText().toString());
                    intent.putExtra(Intent.EXTRA_EMAIL,emember2.getText().toString());
                    intent.putExtra(Intent.EXTRA_EMAIL,emember3.getText().toString());
                    intent.putExtra(Intent.ACTION_DATE_CHANGED,"29-05-2021");
                    if(intent.resolveActivity(getPackageManager())!=null)
                    {
                        startActivity(intent);
                        savee.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(CreateEvent.this, "NO SUCH APP ", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(CreateEvent.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void storeEventData(String ecarpoolname, String ecarpoolevent, String ecarpoollocation, String ecarpoolowner, String emember_1, String emember_2, String emember_3)
    {
        Map<String,String> eData=new HashMap<>();
        eData.put("ename", ecarpoolname);
        eData.put("eevent", ecarpoolevent);
        eData.put("elocation", ecarpoollocation);
        eData.put("eowner", ecarpoolowner);
        eData.put("emember1",emember_1);
        eData.put("emember2",emember_2);
        eData.put("emember3",emember_3);
        firebaseFirestore.collection(user_id).document(String.valueOf(eventid)).set(eData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //progressDialog.dismiss();
                    Toast.makeText(CreateEvent.this, "Event Data is Stored Successfully", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(CreateEvent.this,DisplayEvent.class);
                    startActivity(i);
                    finish();


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(CreateEvent.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                }

            }

        });
    }
}