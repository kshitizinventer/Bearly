package com.example.project1v1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class matchviewadapter extends RecyclerView.Adapter<matchviewadapter.matchviewholder> {

    private List<persons> mpersonlist;
    private Context mcontext;

    private DatabaseReference mref,mref1;
    private FirebaseAuth mauth;

    private List<String> idlist;

    public matchviewadapter(Context context,List<persons> personlist){
        mcontext = context;
        mpersonlist = personlist;
    }

    @NonNull
    @Override
    public matchviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.matchperson_view,parent,false);
        return new matchviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final matchviewholder holder, int position) {
        final persons currentpeson = mpersonlist.get(position);
        final int pos = position;

        holder.match_textView.setText(currentpeson.getName());
        Picasso.get()
                .load(currentpeson.getImageurl())
                .fit()
                .centerCrop()
                .into(holder.match_imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemView.getContext(), chatroom.class);
                intent.putExtra("matchpersonid",currentpeson.getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
       // return 0;

        return mpersonlist.size();
    }

    public class matchviewholder extends RecyclerView.ViewHolder{

        //public ImageView match_imageView;
        public TextView match_textView;
        public CircleImageView match_imageView;

        public matchviewholder(@NonNull View itemView) {
            super(itemView);

            match_imageView = itemView.findViewById(R.id.imageview_matchperson);
            match_textView = itemView.findViewById(R.id.name_matchperson);
        }
    }

}