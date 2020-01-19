package com.example.project1v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chatroom extends AppCompatActivity {

    EditText edx;
    ImageButton sendbtn;
    DatabaseReference mref1,mref2,mref3,Notificationref,mref4;
    FirebaseAuth mauth1;
    messages message1;
    List<messages> messagesList;
    private RecyclerView mRecyclerView;
    private messageAdapter mAdapter;

    String sendername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        edx = findViewById(R.id.chatroom_text_send);
        sendbtn = findViewById(R.id.chatroom_sendbtn);

        messagesList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.chatroom_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Intent intent1 = new Intent();
        Intent intent1 = getIntent();
        final String matchpersonid = intent1.getStringExtra("matchpersonid");


       // the following code is to add message
        mauth1 = FirebaseAuth.getInstance();

        mref1 = FirebaseDatabase.getInstance()
                .getReference("uploads/"+mauth1.getCurrentUser().getUid()+"/chat/"+matchpersonid);

        mref2 = FirebaseDatabase.getInstance()
                .getReference("uploads/"+mauth1.getCurrentUser().getUid()+"/chat/"+matchpersonid);

        mref3 = FirebaseDatabase.getInstance()
                .getReference("uploads/"+matchpersonid+"/chat/"+mauth1.getCurrentUser().getUid());

        Notificationref = FirebaseDatabase.getInstance()
                .getReference("uploads/Notification");

        Log.e("TAG 500", " "+matchpersonid );

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code

              if(!edx.getText().toString().isEmpty()) {

                  mref4 = FirebaseDatabase.getInstance()
                          .getReference("uploads/"+mauth1.getCurrentUser().getUid()+"/person");

                  mref4.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                          sendername = dataSnapshot.child("name").getValue(String.class);

                          String messagetyped = edx.getText().toString();
                          message1 = new messages(mauth1.getCurrentUser().getUid(), edx.getText().toString(), "time");
                          String pushid = mref1.push().getKey();
                          mref1.child(pushid).setValue(message1);

                          String pushid1 = mref3.push().getKey();
                          mref3.child(pushid1).setValue(message1);
                          edx.setText("");

                          // the following code is to to add notification feature
                          HashMap<String,String>  chatNotification = new HashMap<>();
                          chatNotification.put("To",matchpersonid);
                          chatNotification.put("from",mauth1.getCurrentUser().getUid());
                          chatNotification.put("text",messagetyped);
                          chatNotification.put("sendername",sendername + " replied!");

                          String pushid2 = Notificationref.push().getKey();
                          Notificationref.child(pushid2).setValue(chatNotification);

                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });

              }
            }
        });

        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                messagesList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    messages message2 = postSnapshot.getValue(messages.class);
                    messagesList.add(message2);
                }

                mAdapter = new messageAdapter(chatroom.this,messagesList);

                mRecyclerView.setAdapter(mAdapter);
//                mRecyclerView.setSelection(mAdapter.getCount() - 1);
                mRecyclerView.scrollToPosition(messagesList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
