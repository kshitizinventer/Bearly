package com.example.project1v1;

import android.os.Bundle;
import android.util.Log;
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

public class crushFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private matchviewadapter mAdapter;

    private DatabaseReference mreference,mref;
    private List<persons> mpersons;

    private FirebaseAuth mauth;

    private List<String> idlist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crush,container,false);


       // LinearLayoutManager llm = new LinearLayoutManager(getContext());
        //llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = v.findViewById(R.id.recycler_view_crush);
      //  mRecyclerView.setHasFixedSize(true);
        mAdapter = new matchviewadapter(getContext(),mpersons);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        idlist = new ArrayList<>();

        mpersons = new ArrayList<>();


        idlist.add("BOAah7JYCdbK1ndv62MgYneIwKp1");


        mauth = FirebaseAuth.getInstance();

        String uid = mauth.getCurrentUser().getUid();

        mref = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/matches");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshots : dataSnapshot.getChildren()) {

                    if(!postSnapshots.getValue(String.class).equals("none") && postSnapshots.getValue(String.class)!= null){
                        idlist.add(postSnapshots.getValue(String.class));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mreference = FirebaseDatabase.getInstance().getReference("uploads");

        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mpersons.clear();
                for(DataSnapshot postSnapshots : dataSnapshot.getChildren()){
                    persons person2 = postSnapshots.child("person").getValue(persons.class);

                    Log.e("TAG2", "onDataChange: before contains");

                  if(person2 != null)
                   if( idlist.contains(person2.getId()) ) {

                       Log.e("TAG1", "onDataChange: inside contains " );
                       mpersons.add(person2);

                       mAdapter = new matchviewadapter(getContext(),mpersons);
                       mRecyclerView.setAdapter(mAdapter);
                       mAdapter.notifyDataSetChanged();
                   }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }
}
