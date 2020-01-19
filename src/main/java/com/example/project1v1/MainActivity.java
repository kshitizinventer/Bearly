package com.example.project1v1;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
// import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private EditText email,password;
    private Button loginbtn;
    private Button logoutbtn,newactivity;
    private TextView registertxv;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;
    private DatabaseReference mref;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);
        registertxv = findViewById(R.id.mainactivity_textview);
       // logoutbtn = findViewById(R.id.logoutbtn);
       // newactivity = findViewById(R.id.new_activity);


        mAuth = FirebaseAuth.getInstance();

      //   mref = FirebaseDatabase.getInstance().getReference("uploads/"+ mAuth.getCurrentUser().getUid()+"/device_token");

        mAuthlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){


                    FirebaseMessaging.getInstance().subscribeToTopic("weather")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    String msg = getString(R.string.msg_subscribed);
                                    if (!task.isSuccessful()) {
//                                        msg = getString(R.string.msg_subscribe_failed);
                                    }
                                    Log.d("TAG", "msg");
                                    Toast.makeText(MainActivity.this, "msg", Toast.LENGTH_SHORT).show();
                                }
                            });



                    Toast.makeText(MainActivity.this,"sucessfully signed in",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this,Main2Activity.class);
                        startActivity(intent1);
                    }
                else{
                    Toast.makeText(MainActivity.this,"signed out successfully",Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                loginbtn.setEnabled(false);

                if(!emailString.equals("") && !passwordString.equals(""))
                    mAuth.signInWithEmailAndPassword(emailString,passwordString)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        mref = FirebaseDatabase.getInstance().getReference("uploads/"+ mAuth.getCurrentUser().getUid()+"/device_token");
                                        String devicetoken = FirebaseInstanceId.getInstance().getToken();
                                        mref.setValue(devicetoken);
                                    }
                                }
                            });
                else {
                    email.setError("Fields are empty");
                    password.setError("Fields are empty");
                }
            }



        });

        registertxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,signup_activity.class);
                startActivity(intent);
            }
        });


//        logoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//            }
//        });

//        newactivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public void onStart() {
        super.onStart();
       mAuth.addAuthStateListener(mAuthlistner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthlistner != null)
            mAuth.removeAuthStateListener(mAuthlistner);

    }
}
