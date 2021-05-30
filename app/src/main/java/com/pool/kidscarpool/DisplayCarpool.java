package com.pool.kidscarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DisplayCarpool extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFireStoreList;
    private FirestoreRecyclerAdapter adapter;
    Button addcarpool;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String user_id;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_carpool);
        mFireStoreList = findViewById(R.id.firestore_list);
        fAuth = FirebaseAuth.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        addcarpool=findViewById(R.id.addcarpool);
        addcarpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DisplayCarpool.this,CreateCarpool.class);
                startActivity(i);
            }
        });
        user= FirebaseAuth.getInstance().getCurrentUser();
        String currentid=user.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference db=FirebaseFirestore.getInstance().collection("Users").document("user_id");
        Query query =firebaseFirestore.collection(user_id).orderBy("owner");

        FirestoreRecyclerOptions<DetailsFirebase> options = new FirestoreRecyclerOptions.Builder<DetailsFirebase>()
                .setQuery(query, DetailsFirebase.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<DetailsFirebase, DetailsFirebaseViewHolder>(options) {
            @NonNull
            @Override
            public DetailsFirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
                return new DetailsFirebaseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DetailsFirebaseViewHolder holder, int position, @NonNull DetailsFirebase model) {

                        holder.list_carpoolname.setText(model.getCarpool());
                        holder.list_event.setText(model.getEvent());
                        holder.list_location.setText(model.getLocation());
                        holder.list_owner.setText(model.getOwner());
                        holder.list_member1.setText(model.getMember_1());
                        holder.list_member2.setText(model.getMember_2());
                        holder.list_member3.setText(model.getMember_3());

            }
        };

        mFireStoreList.setHasFixedSize(true);
        mFireStoreList.setLayoutManager(new LinearLayoutManager(this));
        mFireStoreList.setAdapter(adapter);


    }
    private class DetailsFirebaseViewHolder extends  RecyclerView.ViewHolder
    {

        private TextView list_carpoolname;
        private TextView list_event;
        private TextView list_location;
        private TextView list_owner;
        private TextView list_member1;
        private TextView list_member2;
        private  TextView list_member3;

        public DetailsFirebaseViewHolder(@NonNull View itemView)
        {
            super(itemView);

            list_carpoolname = itemView.findViewById(R.id.carpoolnametext);
            list_event = itemView.findViewById(R.id.eventtext);
            list_location = itemView.findViewById(R.id.locationtext);
            list_owner = itemView.findViewById(R.id.ownertext);
            list_member1=itemView.findViewById(R.id.member1text);
            list_member2 = itemView.findViewById(R.id.member2text);
            list_member3=itemView.findViewById(R.id.member3text);
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