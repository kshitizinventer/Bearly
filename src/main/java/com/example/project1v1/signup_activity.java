package com.example.project1v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class signup_activity extends AppCompatActivity {

    private EditText name,password,repassword;
    private Button singupbtn;
    FirebaseAuth mauth;
    DatabaseReference mref1,devicetokenref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        name = findViewById(R.id.signup_name);
        password = findViewById(R.id.signup_password);
        repassword = findViewById(R.id.signup_repassword);
        singupbtn = findViewById(R.id.signup_button);
        mauth = FirebaseAuth.getInstance();
        mref1 = FirebaseDatabase.getInstance().getReference("uploads");


        singupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String namestring = name.getText().toString();
                final String passwordstring = password.getText().toString();
                String repasswordstring = repassword.getText().toString();

                if(namestring.isEmpty()){
                    name.setError("Please enter a email id");
                    name.requestFocus();
                }else if(passwordstring.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }else if(!namestring.isEmpty() && !passwordstring.isEmpty() && !repasswordstring.isEmpty()) {

                    if (!passwordstring.equals(repasswordstring)) {
                        repassword.setError("Passwords in both fields don not match");
                    } else {

                        mauth.createUserWithEmailAndPassword(namestring,passwordstring)
                                .addOnCompleteListener(signup_activity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful())
                                            Toast.makeText(signup_activity.this, "Signup Unsuccesful! Try making a aphanumeric password", Toast.LENGTH_LONG).show();
                                        else
                                            mauth.signInWithEmailAndPassword(namestring,passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    startActivity(new Intent(signup_activity.this, profileSetting.class));

                                                    devicetokenref = FirebaseDatabase.getInstance().getReference("uploads/"+ mauth.getCurrentUser().getUid()+"/device_token");
                                                    String devicetoken = FirebaseInstanceId.getInstance().getToken();
                                                    devicetokenref.setValue(devicetoken);

                                                    signup_activity.this.finish();
                                                }
                                            });


                                    }
                                });
                    }
                }

            }
        });

    }
}
