package com.example.project1v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Itsamatch extends AppCompatActivity {

//    private ImageView img1,img2;
    private CircleImageView img1,img2;
    private TextView txv;
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itsamatch);
        img1 = findViewById(R.id.currentpersonimg);
        img2 = findViewById(R.id.matchedpersonimg);



        Intent intent = getIntent();
        String currentpersonid = intent.getStringExtra("currentperson");
        String matchedperson = intent.getStringExtra("matchedperson");

        mref = FirebaseDatabase.getInstance().getReference("uploads/"+currentpersonid+"/person");

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                persons  person1 = dataSnapshot.getValue(persons.class);
                Picasso.get()
                        .load(person1.getImageurl())
                        .fit()
                        .centerCrop()
                        .into(img1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Picasso.get()
                .load(matchedperson)
                .fit()
                .centerCrop()
                .into(img2);
    }
}
