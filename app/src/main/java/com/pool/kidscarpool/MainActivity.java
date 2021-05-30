package com.pool.kidscarpool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pool.kidscarpool.databinding.ActivityMainBinding;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

        private static final String TAG ="TAG" ;
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;
        EditText PhoneNumber, codeEnter;
        Button nextbtn;
        ProgressBar progressBar;
        TextView state;
        CountryCodePicker codePicker;
        String verificationId;
        PhoneAuthProvider.ForceResendingToken Token;
        Boolean verificationInProgress = false;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                fStore = FirebaseFirestore.getInstance();
                fAuth = FirebaseAuth.getInstance();
                PhoneNumber = findViewById(R.id.phone);
                codeEnter = findViewById(R.id.codeEnter);
                progressBar = findViewById(R.id.progressBar);
                nextbtn = findViewById(R.id.nextBtn);
                state = findViewById(R.id.state);
                codePicker = findViewById(R.id.ccp);

                nextbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                openactivity();
                        }
                });


        }

        public void openactivity() {
                if (!verificationInProgress) {
                        if (!PhoneNumber.getText().toString().isEmpty() && PhoneNumber.getText().toString().length() == 10) {

                                String phoneNum = "+" + codePicker.getSelectedCountryCode() + PhoneNumber.getText().toString();
                                Log.d(TAG, "onClick: Phone No -> " + phoneNum);
                                progressBar.setVisibility(View.VISIBLE);
                                state.setText("SendingOTP");
                                state.setVisibility(View.VISIBLE);
                                requestOtp(phoneNum);


                        } else {
                                PhoneNumber.setError("Phone is not valid");
                        }
                } else {
                        String userOTP = codeEnter.getText().toString();
                        if (!userOTP.isEmpty() && userOTP.length() == 6) {
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userOTP);
                                verifyAuth(credential);
                        } else {
                                codeEnter.setError("VALID OTP IS REQUIRED");
                        }
                }
        }

        @Override
        protected void onStart() {
                super.onStart();
                if (fAuth.getCurrentUser() != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("CHECKING");
                        state.setVisibility(View.VISIBLE);
                        checkUserProfile();
                }
        }

        private void checkUserProfile() {
                DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                        startActivity(new Intent(getApplicationContext(), ProfileCreation.class));
                                        finish();
                                } else {
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();
                                }
                        }
                });
        }

        private void verifyAuth(PhoneAuthCredential credential) {
                fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Authentication is Succesfull", Toast.LENGTH_SHORT).show();
                                        checkUserProfile();
                                } else {
                                        Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                        }
                });
        }

        private void requestOtp(String phoneNum) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                progressBar.setVisibility(View.GONE);

                                state.setVisibility(View.INVISIBLE);
                                codeEnter.setVisibility(View.VISIBLE);
                                verificationId = s;
                                Token = forceResendingToken;
                                nextbtn.setText("Verify");

                                nextbtn.setBackgroundColor(Color.BLUE);
                                verificationInProgress = true;

                        }

                        @Override
                        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                                super.onCodeAutoRetrievalTimeOut(s);
                        }

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(MainActivity.this, "Cannot create account " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                });
        }
}