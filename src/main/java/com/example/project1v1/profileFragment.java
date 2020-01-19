package com.example.project1v1;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project1v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class profileFragment extends Fragment {


    private int PICK_IMAGE_REQUEST = 1;
    private Button createroom,joinroom;
    private ImageButton settingbtn;
    private CircleImageView imageView;
    private DatabaseReference rootref,roomref;
    private FirebaseAuth mAuthnew2;
//    private TextView txv;
    private persons person;
    private EditText edx;
    private String tempstring;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View v = inflater.inflate(R.layout.fragment_profile, container, false);

//        logoutbtn = v.findViewById(R.id.fragment_profile_logoutbtn);
        settingbtn = v.findViewById(R.id.fragment_profile_changebtn);
        createroom = v.findViewById(R.id.fragment_profile_createroombtn);
        joinroom = v.findViewById(R.id.fragment_profile_joinroombtn);
        imageView = v.findViewById(R.id.fragment_profile_image);
//        txv = v.findViewById(R.id.fragment_profile_bio);
        edx = v.findViewById(R.id.fragment_profile_edittext);
        mAuthnew2 = FirebaseAuth.getInstance();

       final String uid = mAuthnew2.getCurrentUser().getUid();
    //    rootref = FirebaseDatabase.getInstance().getReference("uploads").child(mAuthnew2.getCurrentUser().getUid());
        rootref = FirebaseDatabase.getInstance().getReference("uploads/"+uid);
        roomref = FirebaseDatabase.getInstance().getReference("uploads/rooms");
//
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                  Toast.makeText(getContext(),mAuthnew2.getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();

                   Log.d("TAG1",mAuthnew2.getCurrentUser().getUid());
                    Log.d("TAG2",postSnapshot.getKey());


                   if(postSnapshot.getKey().toString().equals("person")) {
                        person = postSnapshot.getValue(persons.class);
                     //  Toast.makeText(getContext(), "yo man", Toast.LENGTH_SHORT).show();
//                       txv.setText(person.getBio());
                       Log.d("TAG1", "inside if condition");
                       Picasso.get()
                               .load(person.getImageurl())
                               .fit()
                               .centerCrop()
                               .into(imageView);
//                  String temp = dataSnapshot.getValue(String.class);
//                  txv.setText(temp);

                   }

              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), profileSetting.class);
                startActivity(intent);

            }
        });

//        logoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuthnew2.signOut();
//                Intent intent2 = new Intent(getContext(),MainActivity.class);
//                startActivity(intent2);
//            }
//        });



        createroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Random rand = new Random();
//                int roomid = rand.nextInt(1000);
//
//                room room1 = new room(String.valueOf(roomid),person.getImageurl());
//
//             // making a room and adding the user to the room
//
//               // 1. pushing people into the given roomid
//                roomref = roomref.child(String.valueOf(roomid)).child("people");
//               String pushid =  roomref.push().getKey();
//                 roomref.child(pushid).setValue(person.getId());
//
//                // 2. pushing room img into the roomid
//                 roomref = FirebaseDatabase.getInstance().getReference("uploads/rooms");
//                 roomref = roomref.child(String.valueOf(roomid)).child("image");
//                 roomref.setValue(person.getImageurl());
//
//            // adding room to the person
//                pushid = rootref.child("rooms").push().getKey();
//                rootref.child("rooms").child(pushid).setValue(room1);
//
//                Toast.makeText(getContext(),"room "+roomid + " created",Toast.LENGTH_SHORT).show();
//
////                startActivity(new Intent(getContext(),discoverFragment.class));
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new discoverFragment()).commit();
                startActivity(new Intent(getContext(),creatingroom.class));


            }
        });


        joinroom.setOnClickListener(new View.OnClickListener() {

            room roomtemp;
            DatabaseReference checkroomref,ref;
            List<String> roomlist;
            @Override
            public void onClick(View v) {

                final String roomno  = edx.getText().toString();

//                final String roomno = dialogueboxbuilder();

             // to check if the input room actually exist
                roomlist = new ArrayList<String>();
                checkroomref = FirebaseDatabase.getInstance().getReference("uploads/rooms");

                ref = FirebaseDatabase.getInstance().getReference("uploads/"+uid).child("rooms");

                ref.orderByChild("id").equalTo(roomno).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists()){

                            // 1. pulling out all rooms that exist
                            checkroomref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                        String temp = postSnapshot.getKey();
                                        roomlist.add(temp);

                                        if(postSnapshot.getKey().toString().equals(roomno)) {
                                            roomtemp = new room(postSnapshot.child("roominfo").child("id").getValue(String.class),postSnapshot.child("roominfo").child("name").getValue(String.class),
                                                    postSnapshot.child("roominfo").child("imageurl").getValue(String.class));
                                        }

//                            Log.e("roomlist size", " "+roomlist.size());
                                    }

//

                                    //2. checking if the input room is in roomlist and adding if it exist to the person
                                    if(roomlist.contains(roomno)) {
                                        if (!roomno.equals(" ") && joinroom != null) {



                                            // adding person to the room
                                            roomref = FirebaseDatabase.getInstance().getReference("uploads/rooms/" + roomno + "/people");
                                            String pushid = roomref.push().getKey();
                                            roomref.child(pushid).setValue(person.getId());

                                            // adding the room to the person
                                            rootref = FirebaseDatabase.getInstance().getReference("uploads/" + uid).child("rooms");
                                            pushid = rootref.push().getKey();
                                            rootref.child(pushid).setValue(roomtemp);

//                                            startActivity(new Intent(getContext(),discoverFragment.class));
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                    new discoverFragment()).commit();

                                        }
                                    }else{
                                        Toast.makeText(getContext(),"no such room exist",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }
                        else{
                            Toast.makeText(getContext(), "seems you already are a member of this room", Toast.LENGTH_LONG).show();

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }


        });






        return v;
    }

    public String dialogueboxbuilder(){

        final String newtempstring;
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogue_layout,null);

        final EditText joinroomedx = view.findViewById(R.id.dailogue_edx);
        Button cancelbtn = view.findViewById(R.id.dialogue_cancelbtn);
        Button joinbtn = view.findViewById(R.id.dialogue_joinbtn);

        alert.setView(view);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setTitle("Join Room");

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tempstring = joinroom.getText().toString();
            }
        });

        return tempstring;

    }
}






















