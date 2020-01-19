package com.example.project1v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class creatingroom extends AppCompatActivity {

    private Button uploadbtn, choosebtn;
    private ImageView imageView;
    private Uri imageUri;
    private ProgressBar progressBar;
    private EditText name;
    private int PICK_IMAGE_REQUEST = 1;

    private DatabaseReference rootref, roomref;
    private FirebaseAuth mauth;
    private StorageReference mstorageref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatingroom);

        name = findViewById(R.id.creatingroom_name);
        imageView = findViewById(R.id.creatingroom_imageview);
        progressBar = findViewById(R.id.creatingroom_progressbar);
//        choosebtn = findViewById(R.id.creatingroom_button);
        uploadbtn = findViewById(R.id.creatingroom_uploadbtn);


        roomref = FirebaseDatabase.getInstance().getReference("uploads/rooms");
        mstorageref = FirebaseStorage.getInstance().getReference("uploads/roompics");
        mauth = FirebaseAuth.getInstance();
        rootref = FirebaseDatabase.getInstance().getReference("uploads/" + mauth.getCurrentUser().getUid());


        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadbtn.setEnabled(false);
                uploadFile();
            }
        });


//        choosebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                openFileChooser();
//            }
//        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();
            }
        });

    }


    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        Toast.makeText(creatingroom.this, "choose button pressed", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
            imageUri = data.getData();
        imageView.setImageURI(imageUri);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {

        if (imageUri != null) {
            StorageReference filereference = mstorageref.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            filereference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);


//                                startActivity(new Intent(getApplicationContext(), Main2Activity.class));

                                }
                            }, 500);

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                          // this function is to add people into room and roominfo into people
                            addroomtoFirebase(String.valueOf(downloadUrl));

                           // to transfer user to discover fragment
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new discoverFragment()).commit();

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(creatingroom.this,e.getMessage(),Toast.LENGTH_SHORT);
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
            Toast.makeText(creatingroom.this,"Please fill all the details !",Toast.LENGTH_SHORT).show();

        }

    }

    private void addroomtoFirebase(String imageurl){
        Random rand = new Random();
        int roomid = rand.nextInt(1000);

        String roomname = name.getText().toString();

        room room1 = new room(String.valueOf(roomid),roomname,imageurl);

        // making a room and adding the user to the room

        // 1. pushing people into the given roomid
        roomref = roomref.child(String.valueOf(roomid)).child("people");
        String pushid = roomref.push().getKey();
        roomref.child(pushid).setValue(mauth.getCurrentUser().getUid());

        // 2. pushing room info  into the roomid
        roomref = FirebaseDatabase.getInstance().getReference("uploads/rooms");
        roomref = roomref.child(String.valueOf(roomid)).child("roominfo");
        roomref.setValue(room1);

        // adding room to the person
        pushid = rootref.child("rooms").push().getKey();
        rootref.child("rooms").child(pushid).setValue(room1);

        Toast.makeText(creatingroom.this, "room " + roomid + " created", Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(getContext(),discoverFragment.class));
    }

}
