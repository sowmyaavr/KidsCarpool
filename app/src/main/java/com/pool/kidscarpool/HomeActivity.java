package com.pool.kidscarpool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    CardView carpool, friends,events,profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        carpool = findViewById(R.id.b1);

        carpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,DisplayCarpool.class);
                startActivity(i);
            }
        });
        events = findViewById(R.id.eventid);
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,DisplayEvent.class);
                startActivity(i);
            }
        });

        friends = findViewById(R.id.friends);
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent friend = new Intent(HomeActivity.this,AddFriends.class);
                startActivity(friend);
            }
        });
        profile=findViewById(R.id.b4);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile=new Intent(HomeActivity.this,UserProfile.class);
                startActivity(profile);
            }
        });

    }
}