package com.example.project1v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class createroom extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private personAdapter mAdapter;

    private DatabaseReference mreference,mref,mref2,mref3;
    private List<persons> mpersons;
    private List<String> personsinroom,personalreadyliked,matches;
    private List<Integer> status;

    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroom);

        mpersons = new ArrayList<>();

        Intent intent = getIntent();
        String roomno = intent.getStringExtra("roomno");

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(createroom.this));
        mAdapter = new personAdapter(createroom.this,mpersons,status);
        mRecyclerView.setAdapter(mAdapter);

        mauth = FirebaseAuth.getInstance();

        final String uid = mauth.getCurrentUser().getUid();


        personalreadyliked = new ArrayList<>();
        personsinroom = new ArrayList<>();
        matches = new ArrayList<>();
        status = new ArrayList<>();

        mreference = FirebaseDatabase.getInstance().getReference("uploads");
        Log.e("TAG 15", " "+roomno);
        mref = FirebaseDatabase.getInstance().getReference("uploads/rooms/"+roomno+"/people");
       mref2 = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/likes");
       mref3 = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/matches");



        /////////////////////////////////////

//        mref2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//              //  mpersons.clear();
////                codefordisplayingpeopleinroom(uid);
////                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        codefordisplayingpeopleinroom(uid);
        mAdapter.notifyDataSetChanged();



    }


    public void codefordisplayingpeopleinroom(final String uid){

        // this event listner collects all the person who have joined a given particular room
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    personsinroom.add(postSnapshot.getValue(String.class));
                }


                // here is the code for the people i have already liked in the room

                mref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot postsnapshot : dataSnapshot.getChildren()){
                            personalreadyliked.add(postsnapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // here is the code for people i have matched with

                mref3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                            matches.add(postSnapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // here is the code for collecting persons in this particular room

                mreference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                      //  int status = 0;

                        for(DataSnapshot postSnapshots : dataSnapshot.getChildren()){
                            persons person2 = postSnapshots.child("person").getValue(persons.class);

//                            if(person2 != null) {
//                                if (matches.contains(person2.getId()))
//                                    status.add(2);
//                                else if (personalreadyliked.contains(person2.getId()))
//                                    status.add(1);
//                                else
//                                    status.add(0);
//                            }

                            if(person2 != null)
                                if(personsinroom.contains(person2.getId()) && !person2.getId().equals(uid) /*&& !personalreadyliked.contains(person2.getId())*/ ) {
                                    Log.e("tag 101", " "+ person2.getId());
                                    Log.e("TAG 102", " "+ uid );
                                    mpersons.add(person2);

                                    if (matches.contains(person2.getId()))
                                        status.add(2);
                                    else if (personalreadyliked.contains(person2.getId()))
                                        status.add(1);
                                    else
                                        status.add(0);
                                }




                           if(person2 != null)
                            Log.e("current person", " "+person2.getId() );

                          for(int i = 0;i<matches.size();i++)
                              Log.e("contents of matchlist", " "+matches.get(i));

                            Log.e("TAG new"," "+matches.size() );
                            Log.e("TAG new2 ", " "+ status );
                            mAdapter = new personAdapter(createroom.this,mpersons,status);

                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
