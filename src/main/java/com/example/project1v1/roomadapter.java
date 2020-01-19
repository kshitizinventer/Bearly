package com.example.project1v1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class roomadapter extends RecyclerView.Adapter<roomadapter.roomviewholder> {

    private Context mconterxt;
    private List<room> mrooms;
    private persons person1;

    public roomadapter(Context mconterxt, List<room> mrooms,persons person1) {
        this.mconterxt = mconterxt;
        this.mrooms = mrooms;
        this.person1 = person1;
    }

    @NonNull
    @Override
    public roomviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      //  return null;

        View v = LayoutInflater.from(mconterxt).inflate(R.layout.roomlistview,parent,false);
        return new roomviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull roomviewholder holder, int position){

        final room currentroom = mrooms.get(position);
        holder.roomname.setText(currentroom.getName());

       //if(person1 != null)
        Picasso.get()
                .load(currentroom.getImageurl())
                .fit()
                .centerCrop()
                .into(holder.roomimg);
        Log.e("TAG10"," "+currentroom.getImageurl());

      final  roomviewholder tempholder = holder;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(tempholder.itemView,currentroom.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
       // return 0;

        return mrooms.size();
    }

   // the class i self constructed to hold roomview
    public class roomviewholder extends RecyclerView.ViewHolder{

        TextView roomname;
       // ImageView roomimg;
        CircleImageView roomimg;

        public roomviewholder(@NonNull View itemView) {
            super(itemView);

            roomname = itemView.findViewById(R.id.name_roomlistview);
            roomimg = itemView.findViewById(R.id.imageview_roomlistview);
        }
    }

    public void openActivity(View v,String roomname){

        Intent intent = new Intent(v.getContext(),createroom.class);
        intent.putExtra("roomno",roomname);
        mconterxt.startActivity(intent);
    }

}
