package com.pool.kidscarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyPermission;

import id.zelory.compressor.Compressor;

public class ProfileCreation extends AppCompatActivity {
    private ImageView userImage;
        private EditText userName, userPhone, userAddress,userChildName;
        private Button submit,addchildbutton,fetch;
        String Storage_Path = "All_Image_Uploads/";
        private ProgressDialog progressDialog;
        private Uri imageUri = null;
    FirebaseAuth fAuth;
    FirebaseUser user;
        private StorageReference storageReference;
        FirebaseAuth firebaseAuth;
        private FirebaseFirestore firebaseFirestore;
         String user_id;
        private Bitmap compressed;
        LinearLayout l;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile_creation);
            progressDialog = new ProgressDialog(this);
            userImage = findViewById(R.id.user_image);
            userName = findViewById(R.id.user_name);
            userPhone = findViewById(R.id.user_email);
            userAddress = findViewById(R.id.user_address);
            userChildName = findViewById(R.id.add_child1);
            fAuth = FirebaseAuth.getInstance();


            submit = findViewById(R.id.submit);
            l = findViewById(R.id.addchildprofile_id);
            addchildbutton = findViewById(R.id.addchildbutton);
            firebaseAuth = FirebaseAuth.getInstance();
            user_id = firebaseAuth.getCurrentUser().getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(ProfileCreation.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(ProfileCreation.this, "Permission Denied", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(ProfileCreation.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            choseImage();
                        }
                    } else {
                        choseImage();
                    }
                }
            });

            addchildbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View child = getLayoutInflater().inflate(R.layout.child, null);
                    l.addView(child);
                }
            });


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.setMessage("Storing Data...");
                    progressDialog.show();
                    final String username = userName.getText().toString();
                    final String userphone = userPhone.getText().toString();
                    final String useradress = userAddress.getText().toString();
                    final String userchildname = userChildName.getText().toString();
                    if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userphone) && !TextUtils.isEmpty(useradress) && imageUri != null && !TextUtils.isEmpty(userchildname)) {
                        File newFile = new File(imageUri.getPath());
                        try {
                            compressed = new Compressor(ProfileCreation.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] thumbData = byteArrayOutputStream.toByteArray();
                        UploadTask image_path = storageReference.child("user_image").child(user_id + ".jpg").putBytes(thumbData);
                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeUserData(task, username, userphone, useradress, userchildname);

                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(ProfileCreation.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ProfileCreation.this, "Fill all details", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



        public String GetFileExtension(Uri imageUri) {

            ContentResolver contentResolver = getContentResolver();

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

            // Returning the file Extension.
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri)) ;

        }

        private void storeUserData(Task<UploadTask.TaskSnapshot> task, String username, String userphone, String useradress,String userchildname) {
            Uri download_uri ;
            if (task != null) {

                task.getException();
            }
            else {
                download_uri = imageUri;
            }
            Map<String, String> userData = new HashMap<>();
            userData.put("username",username);
            userData.put("userphone",userphone);
            userData.put("useraddress",useradress);
            userData.put("userimage",imageUri.toString());
            userData.put("userchildname",userchildname);
            firebaseFirestore.collection("Users").document(user_id).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileCreation.this, "User Data is Stored Successfully", Toast.LENGTH_LONG).show();
                       Intent mainIntent = new Intent(ProfileCreation.this, UserProfile.class);
                       startActivity(mainIntent);

                       finish();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ProfileCreation.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }

            });
        }



    private void choseImage() {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(ProfileCreation.this);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    imageUri = result.getUri();
                    userImage.setImageURI(imageUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            }


        }



    }





