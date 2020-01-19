package com.example.project1v1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class discoverFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private roomadapter mAdapter;

    private DatabaseReference mreference,mref2;
    private List<room> mrooms;

    private FirebaseAuth mauth;
    private persons person1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_discover,container,false);

        mRecyclerView = v.findViewById(R.id.recycler_view_roomlist);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mauth = FirebaseAuth.getInstance();

        String uid = mauth.getCurrentUser().getUid();

        mrooms = new ArrayList<>();
        mreference = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/rooms");

        mref2 = FirebaseDatabase.getInstance().getReference("uploads/"+uid);

        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshots : dataSnapshot.getChildren()){
                    room room1 = postSnapshots.getValue(room.class);
                    mrooms.add(room1);

                    mref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            person1 = dataSnapshot.child("person").getValue(persons.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    mAdapter = new roomadapter(getContext(),mrooms,person1);

                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
 //        return inflater.inflate(R.layout.fragment_discover,container,false);
        return v;
    }
}
