package com.jramdev.bandssocialnetwork;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<Chat> chatList;
    String imageUrl;
    FirebaseUser fUser;
    public AdapterChat(Context context, List<Chat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, viewGroup, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, viewGroup, false);
            return new MyHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        String mensaje = chatList.get(i).getMensaje();
        String timeStamp = chatList.get(i).getTimeStamp();
        //convert time stamp to dd/
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
        holder.mensajeTv.setText(mensaje);
        holder.timetv.setText(dateTime);
        try {
            Picasso.get().load(imageUrl).into(holder.profileIv);
        } catch (Exception ex) {
            Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(holder.profileIv);
        }
        //set seen/delivered status of message
        //minute 16:54
        if (i == chatList.size() - 1) {
            if (chatList.get(i).isSeen()) {
                holder.isSeenTv.setText("Visto"); }
            else{
                holder.isSeenTv.setText("Enviado"); }
        }else{
            holder.isSeenTv.setVisibility(View.GONE); }
    }
    @Override
    public int getItemCount() {
        return chatList.size();
    }
    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getEmisor().equals(fUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView profileIv;
        TextView mensajeTv, timetv, isSeenTv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //iniciar vistas
            profileIv = itemView.findViewById(R.id.profileIV);
            mensajeTv = itemView.findViewById(R.id.messageTV);
            timetv = itemView.findViewById(R.id.timeTV);
            isSeenTv = itemView.findViewById(R.id.isSeen);
        }
    }
}
