package com.example.project1v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class profileSetting extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    private Button uploadbtn,choosebtn,logoutbtn;
    private ImageView imageView;
    private Uri imageUri;
    private ProgressBar progressBar;
    private EditText bio,name,branch,year;

    private FirebaseAuth mAuthnew;

    private StorageReference mstorageref;
    private DatabaseReference mdatabaseref,mref1;
    private persons person1;
    boolean  everythingok = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        choosebtn = findViewById(R.id.profile_setting_choose);
        uploadbtn = findViewById(R.id.profile_setting_applybtn);
        imageView = findViewById(R.id.profile_setting_img);
        progressBar = findViewById(R.id.profile_setting_progressbar);

        bio = findViewById(R.id.profile_setting_bio);
        name = findViewById(R.id.profile_setting_name);
        branch = findViewById(R.id.profile_setting_branch);
        year = findViewById(R.id.profile_Setting_year);
        logoutbtn = findViewById(R.id.fragment_profile_logoutbtn);

        mAuthnew = FirebaseAuth.getInstance();
        mstorageref = FirebaseStorage.getInstance().getReference("uploads");
        mdatabaseref = FirebaseDatabase.getInstance().getReference("uploads");
        mref1 = FirebaseDatabase.getInstance().getReference("uploads/"+mAuthnew.getCurrentUser().getUid()+"/person");

        mref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    person1 = dataSnapshot.getValue(persons.class);

                  if(person1 != null) {
                      Picasso.get()
                              .load(person1.getImageurl())
                              .fit()
                              .centerCrop()
                              .into(imageView);
                      bio.setText(person1.getBio());
                      name.setText(person1.getName());
                      branch.setText(person1.getName());
                      year.setText(person1.getYear());
                  }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
//                uploadbtn.setEnabled(false);
//                logoutbtn.setEnabled(false);
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthnew.signOut();
                Intent intent2 = new Intent(profileSetting.this,MainActivity.class);
                startActivity(intent2);
            }
        });

        }

    private void openFileChooser()
    {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
        Toast.makeText(profileSetting.this,"choose button pressed",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // imageUri = person1.getImageurl();
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
            imageUri = data.getData();
        //imageView.setImageURI(imageUri);
        Picasso.get()
                .load(imageUri)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile()
    {
       if(imageUri != null)
        if(imageUri != null && !isEmpty(name) && !isEmpty(bio) && !isEmpty(year) && !isEmpty(branch))
        {
            uploadbtn.setEnabled(false);
            logoutbtn.setEnabled(false);

//            if(imageUri == null) {
//                try {
//                   URI uri = new URI(person1.getImageurl());
//
//                }
//                catch (URISyntaxException e) {
//
//                }
//            }

            StorageReference filereference = mstorageref.child(System.currentTimeMillis()
            +"."+getFileExtension(imageUri));

            filereference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);

                                   if(everythingok)
                                    startActivity(new Intent(getApplicationContext(),Main2Activity.class));

                                }
                            },500);

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            String imageurl1 = String.valueOf(downloadUrl);

                            String UID = mAuthnew.getCurrentUser().getUid();
                            String name1 = name.getText().toString();
                            String bio1 = bio.getText().toString();
                            String branch1 = branch.getText().toString();
                            String year1 = year.getText().toString();
                            //String imageurl1 =  taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();


                            if(!name1.isEmpty() && !bio1.isEmpty() && !branch1.isEmpty() && !year1.isEmpty())
                            {
                                persons person = new persons(bio1,branch1,imageurl1,name1,year1,UID);
                                mdatabaseref.child(UID).child("person").setValue(person);
                                everythingok = true;
                            }else{
                                Toast.makeText(profileSetting.this,"description not filled out right",Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profileSetting.this,e.getMessage(),Toast.LENGTH_SHORT);
                            uploadbtn.setEnabled(true);
                            logoutbtn.setEnabled(true);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressBar.setProgress((int) progress);
                        }
                    });
        }else{
            Toast.makeText(profileSetting.this,"Please fill out all description",
                    Toast.LENGTH_SHORT).show();
        }
       else
           Toast.makeText(profileSetting.this,"Please select a profile too, if you already have a pic still choose again",
                   Toast.LENGTH_LONG).show();

    }

    private boolean isEmpty(EditText eText){
        if(eText.getText().toString().trim().length() > 0)
            return false;
        return true;

    }
}
