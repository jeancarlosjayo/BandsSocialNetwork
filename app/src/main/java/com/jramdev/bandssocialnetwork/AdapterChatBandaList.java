package com.jramdev.bandssocialnetwork;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterChatBandaList extends RecyclerView.Adapter<AdapterChatBandaList.MyHolder> {
    Context mContext;
    List<BandaUser> userList;

    private HashMap<String, String> lastMessageMap;

    public AdapterChatBandaList(Context mContext, List<BandaUser> userList) {
        this.mContext = mContext;
        this.userList = userList;
        lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_chatlist, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myholder, int i) {
        final String hisUid = userList.get(i).getUid();
        String userImage = userList.get(i).getProfilepic();
        String userName = userList.get(i).getNameband();
        String lastMessage = lastMessageMap.get(hisUid);
        myholder.nameTv.setText(userName);
        if (lastMessage == null || lastMessage.equals("default")) {
            myholder.lastMessageTv.setVisibility(View.GONE);
        } else {
            myholder.lastMessageTv.setVisibility(View.VISIBLE);
            myholder.lastMessageTv.setText(lastMessage);
        }
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(myholder.profileIv);
        } catch (Exception ex) {
            Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(myholder.profileIv);
        }
        //set online status
        if (userList.get(i).getOnlineStatus().equals("online")) {
            myholder.onlineStatusIv.setImageResource(R.drawable.circle_online);

        } else {
            myholder.onlineStatusIv.setImageResource(R.drawable.circle_offline);
        }
        //click of user in chatlist
        myholder.itemView.setOnClickListener(v -> {
            //start chat
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra("SuUID", hisUid);
            mContext.startActivity(intent);
        });
    }

    public void setLastMessageMap(String userId, String lastMessage) {
        lastMessageMap.put(userId, lastMessage);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        //vistas
        ImageView profileIv, onlineStatusIv;
        TextView nameTv, lastMessageTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileIv);
            onlineStatusIv = itemView.findViewById(R.id.onlineStatusIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            lastMessageTv = itemView.findViewById(R.id.lastMessageTV);


        }
    }
}
