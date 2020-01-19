package com.example.project1v1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.messageViewHolder> {

    Context mcontext;
    static List<messages> messagesList;
    static FirebaseAuth mauth;

    public static class messageViewHolder extends RecyclerView.ViewHolder{

        public TextView messagetext;
        public messageViewHolder(@NonNull View itemView) {
            super(itemView);
//           if(getItemViewType() == 1)
            messagetext = itemView.findViewById(R.id.right_message);
//           else
//               messagetext = itemView.findViewById(R.id.left_message);
        }
    }

    public messageAdapter(Context mcontext, List<messages> messagesList) {
        this.mcontext = mcontext;
        this.messagesList = messagesList;
        mauth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View V;

        if(viewType == 1)
            V = LayoutInflater.from(mcontext.getApplicationContext()).inflate(R.layout.rightmessage_view,parent,false);
        else
            V = LayoutInflater.from(mcontext.getApplicationContext()).inflate(R.layout.leftmessage_view,parent,false);
        messageViewHolder mvh = new messageViewHolder(V);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull messageViewHolder holder, int position) {

        holder.messagetext.setText(messagesList.get(position).getMessage().toString());
    }

    @Override
    public int getItemCount() {
//        return 0;
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if(mauth.getCurrentUser().getUid().equals(messagesList.get(position).getUserid()))
            return 1;
        else
            return 0;
    }
}
