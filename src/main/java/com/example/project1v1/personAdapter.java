package com.example.project1v1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class personAdapter extends RecyclerView.Adapter<personAdapter.personView> {

    Context mContext;
    List<persons> mpersonlist;
    DatabaseReference mref,mref1,mref2,mref3,mreftemp1,mreftemp2,mref4,Notificationref;
    FirebaseAuth mAuth3;
    List<Integer> status;
    int tempflag,notificationcounter,count;


    private ArrayList<String> idlist,matchlist;

    private OnItemClickListner mlistner;

    public interface OnItemClickListner{
        void OnItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        mlistner = listner;
    }

    public personAdapter(Context context, List<persons> personsList,List<Integer> status)
    {
        mContext = context;
        mpersonlist = personsList;

        idlist = new ArrayList<String>();
        matchlist = new ArrayList<String>();

        this.status = status;
       // matchlist.add("testing");

        tempflag = 0;
        notificationcounter = 0;
        count = 0;

    }

    @NonNull
    @Override
    public personView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // return null;
        View v = LayoutInflater.from(mContext).inflate(R.layout.person_view,parent,false);
        return new personView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final personView holder, final int position) {
        final persons currentperson = mpersonlist.get(position);
        if(currentperson != null)
         holder.bio.setText(currentperson.getBio());
         final int pos = position;


         final int statust = status.get(position);

//         if(statust == 2) {
//             holder.itemView.findViewById(R.id.cardView).setBackgroundColor(Color.parseColor("#add8e6"));
//             Log.e("TAG 2222", "status  = 2" );
//         }else if(statust == 1) {
//             holder.itemView.findViewById(R.id.cardView).setBackgroundColor(Color.parseColor("#a4c639"));
//             Log.e("TAG 1111", "status = 1" );
//         }
//

        if(statust == 2) {
          ImageView iv = holder.itemView.findViewById(R.id.person_view_rightbtn);
          iv.setColorFilter(Color.parseColor("#add8e6"));
            Log.e("TAG 2222", "status  = 2" );
        }else if(statust == 1) {
            ImageView iv = holder.itemView.findViewById(R.id.person_view_rightbtn);
            iv.setColorFilter(Color.parseColor("#a4c639"));
            Log.e("TAG 1111", "status = 1" );
        }


        mAuth3 = FirebaseAuth.getInstance();
         final String uid = mAuth3.getCurrentUser().getUid();

         mref4 = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/matches");



         mref4.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                 for(DataSnapshot postsnapshot : dataSnapshot.getChildren()){
                     if( currentperson.getId().equals( postsnapshot.getValue(String.class) ) ){

                       //  persons logedinperson = dataSnapshot.child("person").getValue(persons.class);
                         personView tempholder1 = holder;
//                         tempholder1.itemView.findViewById(R.id.cardView).setBackgroundColor(Color.parseColor("#add8e6"));
                         ImageView iv = tempholder1.itemView.findViewById(R.id.person_view_rightbtn);
                         iv.setColorFilter(Color.parseColor("#add8e6"));
//                         Log.e("this is a test", " "+notificationcounter );
//                        if(notificationcounter > 0)
//                        {   Intent intent =  new Intent(holder.itemView.getContext(),Itsamatch.class);
//                             intent.putExtra("currentperson",uid);
//                             intent.putExtra("matchedperson",currentperson.getImageurl());
//                            holder.itemView.getContext().startActivity(intent);
//                             notificationcounter = -1000;
//                        }

//                         updatenotificationcounter();

                     }
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });



        holder.rightbtn.setOnClickListener(new View.OnClickListener() {

            ArrayList<String> firebaselikelist = new ArrayList<String>();
            personView tempholder = holder;
            @Override
            public void onClick(View view) {

                if(statust != 2) {

//                    View V = tempholder.itemView.findViewById(R.id.cardView);
//                    V.setBackgroundColor(Color.parseColor("#a4c639"));
                    ImageView iv2 = tempholder.itemView.findViewById(R.id.person_view_rightbtn);
                     iv2.setColorFilter(Color.parseColor("#a4c639"));


                }



               final  persons newperson = mpersonlist.get(pos);
                String name = newperson.getName();
                mAuth3 = FirebaseAuth.getInstance();


                final String uid = mAuth3.getCurrentUser().getUid();



                         //code for adding who i like

                         mref = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/likes");

                       mref.orderByValue().equalTo(newperson.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                               if(!dataSnapshot.exists()){

                                   String pushid;
                                   pushid = mref.push().getKey();
                                   mref.child(pushid).setValue(newperson.getId());

                                   // code for adding who likes you
                                   personView tempholder;
                                   tempholder = holder;
                                    addwholikesyou(uid,newperson);


                                   //code for checking if there is a match and adding the match to current user and liked person
                                  int isthereamatch =  matchadder(uid,newperson);

                                 if(isthereamatch == 1) {
                                    // tempholder.itemView.findViewById(R.id.cardView).setBackgroundColor(Color.parseColor("#add8e6"));
                                     ImageView iv = tempholder.itemView.findViewById(R.id.person_view_rightbtn);
                                     iv.setColorFilter(Color.parseColor("#a4c639"));
                                     Log.e("TAG 1000", " its a match");

                                     // code for sending a notification
                                     sendNotification(newperson.getId(),1);

                                       // code for showing its a Match
                                          Intent intent =  new Intent(holder.itemView.getContext(),Itsamatch.class);
                                          intent.putExtra("currentperson",uid);
                                          intent.putExtra("matchedperson",currentperson.getImageurl());
                                          holder.itemView.getContext().startActivity(intent);
                                          notificationcounter = -1000;

                                 }else {
                                     Log.e("TAG 1000", "its not a match");
                                    // tempholder.itemView.findViewById(R.id.cardView).setBackgroundColor(Color.parseColor("#a4c639"));
                                     ImageView iv = tempholder.itemView.findViewById(R.id.person_view_rightbtn);
                                    // iv.setColorFilter(Color.parseColor("#ffd800"));

                                     // code for sending a notification
                                     sendNotification(newperson.getId(),0);

                                 }



                               }else{
                                   Toast.makeText(mContext, "you already like this person", Toast.LENGTH_SHORT).show();
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });



            } // end of view of right button click
        });


        // the following is the code for disklike button



        holder.leftbtn.setOnClickListener(new View.OnClickListener() {

            ArrayList<String> firebaselikelist = new ArrayList<String>();

            @Override
            public void onClick(View v) {

                mAuth3 = FirebaseAuth.getInstance();

                final String uid = mAuth3.getCurrentUser().getUid();
                final  persons newperson = mpersonlist.get(pos);


                // code for retrieving who i already like and dislike...........................................

                mreftemp1 = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/likes");

                mreftemp1.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren() ){

                            if(ds != null)
                                firebaselikelist.add(ds.getValue(String.class));
                        }

                        //code for adding who i like

                        mref = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/likes");

                        String pushid;
                        Log.e("TAG 99", " "+firebaselikelist.size() );

                        if(!firebaselikelist.contains(newperson.getId())) {
                            pushid = "-1";
                            mref.child(pushid).setValue(newperson.getId());
                        }else{
                            Toast.makeText(holder.itemView.getContext(),"you already like this person",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

       if(currentperson != null)
        Picasso.get()
                .load(currentperson.getImageurl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
       if(currentperson != null) {
           holder.name.setText(currentperson.getName());
           DatabaseReference mtempref;
           mtempref = FirebaseDatabase.getInstance().getReference("uploads/"+ currentperson.getId() + "/likesyou");
           mtempref.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   count = 0;
                   for(DataSnapshot postsnapshot : dataSnapshot.getChildren())
                       count++;
                   holder.likes.setText(count + " likes");
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

       }

    }

    @Override
    public int getItemCount() {
      //  return 0;
        return mpersonlist.size();
    }

    public class personView extends RecyclerView.ViewHolder{

        public TextView bio,name,likes;
        public ImageView imageView;
//        public Button leftbtn;
        ImageView leftbtn,rightbtn;

        public personView(@NonNull View itemView) {
            super(itemView);

            bio = itemView.findViewById(R.id.person_view_bio);
            name = itemView.findViewById(R.id.person_view_name);
            likes = itemView.findViewById(R.id.person_view_likes);
            imageView = itemView.findViewById(R.id.person_view_image);
            leftbtn = itemView.findViewById(R.id.person_view_leftbtn);
            rightbtn = itemView.findViewById(R.id.person_view_rightbtn);
        }

    }

    public void addwholikesyou(final String uid,final persons newperson){


        //code for adding who likes you
        mref1 = FirebaseDatabase.getInstance().getReference("uploads/"+newperson.getId()+"/likesyou");

        mref1.orderByValue().equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(!dataSnapshot.exists()){

                    for(DataSnapshot postsnapshot : dataSnapshot.getChildren()){
                        count++;
                    }
                    String pushid;
                    pushid = mref1.push().getKey();
                    mref1.child(pushid).setValue(uid);
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public int matchadder(final String uid,final persons newperson){

        // code for checking if there is a match and adding match to user and liked person

        tempflag = 0;
        mref2 = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/likesyou");

        mref2.orderByValue().equalTo(newperson.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // adding match to current user
                    mreftemp1 = FirebaseDatabase.getInstance().getReference("uploads/"+uid+"/matches");
                    String pushid = mreftemp1.push().getKey();
                    mreftemp1.child(pushid).setValue(newperson.getId());


                    // adding match to liked person
                    mreftemp1 = FirebaseDatabase.getInstance().getReference("uploads/"+newperson.getId()+"/matches");
                    pushid = mreftemp1.push().getKey();
                    mreftemp1.child(pushid).setValue(uid);

                    tempflag = 1;
                    Log.e("tempflag", " "+tempflag );


                }else{
                    Log.e("tempflag", "tempflag is still 0 " );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        if(tempflag == 1)
            return 1;
        else
            return 0;
    }

    void updatenotificationcounter(){
        notificationcounter++;
    }

    public void sendNotification(String recieverid,int match){

        Notificationref = FirebaseDatabase.getInstance().getReference("uploads/Notification");

        HashMap<String,String> chatNotification = new HashMap<>();

       if(match == 0) {
           chatNotification.put("To", recieverid);
           chatNotification.put("from", mAuth3.getCurrentUser().getUid());
           chatNotification.put("text", "You are halfway from a match!");
           chatNotification.put("sendername", "Some one new liked you!");
       }else
           if(match == 1){
               chatNotification.put("To", recieverid);
               chatNotification.put("from", mAuth3.getCurrentUser().getUid());
               chatNotification.put("text", "Say hello to your new Crush!");
               chatNotification.put("sendername", "Its a Match !");
           }

        Notificationref.push().setValue(chatNotification);

    }


}
