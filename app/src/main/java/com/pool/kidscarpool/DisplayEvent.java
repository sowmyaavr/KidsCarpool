package com.pool.kidscarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DisplayEvent extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView eventRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    Button addeventbtn;
    private Context context;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String user_id;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event);
        addeventbtn=findViewById(R.id.addevent);
        addeventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DisplayEvent.this,CreateEvent.class);
                startActivity(i);
            }
        });
        eventRecyclerView = findViewById(R.id.firestore_event_list);
        fAuth = FirebaseAuth.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        user= FirebaseAuth.getInstance().getCurrentUser();
        String currentid=user.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference db=FirebaseFirestore.getInstance().collection("Users").document("user_id");
        Query query =firebaseFirestore.collection(user_id).orderBy("eowner");

        FirestoreRecyclerOptions<EventClassDetails> options = new FirestoreRecyclerOptions.Builder<EventClassDetails>()
                .setQuery(query, EventClassDetails.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<EventClassDetails, DisplayEvent.EventClassDetailsViewHolder>(options) {
            @NonNull
            @Override
            public EventClassDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_event_item_single,parent,false);
                return new DisplayEvent.EventClassDetailsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DisplayEvent.EventClassDetailsViewHolder holder, int position, @NonNull EventClassDetails model) {

                holder.list_Ecarpoolname.setText(model.getEname());
                holder.list_Eevent.setText(model.getEevent());
                holder.list_Elocation.setText(model.getElocation());
                holder.list_Eowner.setText(model.getEowner());
                holder.list_Emember1.setText(model.getEmember1());
                holder.list_Emember2.setText(model.getEmember2());
                holder.list_Emember3.setText(model.getEmember3());
                holder.startevent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                  //      Toast.makeText(context, "Event Started", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + 12.9858+ "," +  80.1070 + "&daddr=" + 12.9425 + "," +80.1416));
                        startActivity(intent);
                    }
                });

            }
        };

        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(adapter);


    }
    private class EventClassDetailsViewHolder extends  RecyclerView.ViewHolder
    {

        private TextView list_Ecarpoolname;
        private TextView list_Eevent;
        private TextView list_Elocation;
        private TextView list_Eowner;
        private TextView list_Emember1;
        private TextView list_Emember2;
        private  TextView list_Emember3;
        private Button startevent;

        public EventClassDetailsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            list_Ecarpoolname = itemView.findViewById(R.id.ecarpoolnametext);
            list_Eevent = itemView.findViewById(R.id.eeventtext);
            list_Elocation = itemView.findViewById(R.id.elocationtext);
            list_Eowner = itemView.findViewById(R.id.eownertext);
            list_Emember1=itemView.findViewById(R.id.emember1text);
            list_Emember2 = itemView.findViewById(R.id.emember2text);
            list_Emember3=itemView.findViewById(R.id.emember3text);
            startevent=itemView.findViewById(R.id.startevent);
        }

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }



}