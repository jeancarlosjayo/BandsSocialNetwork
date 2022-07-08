package com.jramdev.bandssocialnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    //Vistas
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nametv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference UsersdbReference;
    ImageButton BackButton;
    ValueEventListener seenListener;
    DatabaseReference userRefforSeen;

    List<Chat> chatList;
    AdapterChat adapterChat;

    String hisUid;
    String myUid;
    String hisImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//init views
        toolbar = findViewById(R.id.toolbarchat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = findViewById(R.id.chat_recyclerview);
        profileIv = findViewById(R.id.profilepic);
        nametv = findViewById(R.id.nameTvchat);
        userStatusTv = findViewById(R.id.userStatusTv);
        messageEt = findViewById(R.id.message_id);
        sendBtn = findViewById(R.id.sendBtn);
        userStatusTv = findViewById(R.id.userStatusTv);
        BackButton = findViewById(R.id.backbtn);
        //Layout(LinearLayout) for Reyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //recyclerview properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Recoge datos para el usuario de chat
        Intent intent = getIntent();
        hisUid = intent.getStringExtra("SuUID");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        UsersdbReference = firebaseDatabase.getReference("users");
        BackButton.setOnClickListener(v -> {
            finish();
        });
        Query userquery = UsersdbReference.orderByKey().equalTo(hisUid);
        userquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //revisar
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String type = "" + ds.child("typeuser").getValue();
                    if (type.equals("promotor")) {
                        String name = "" + ds.child("fullname").getValue();
                        hisImage = "" + ds.child("profilepic").getValue();
                        //Revisar el estado de tipeo
                        //set data
                        nametv.setText(name);
                        try {
                            Picasso.get().load(hisImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(profileIv);
                        } catch (Exception ex) {
                            Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(profileIv);
                        }
                    }
                    if (type.equals("cliente")) {
                        String name = "" + ds.child("fullname").getValue();
                        hisImage = "" + ds.child("profilepic").getValue();
                        //Revisar el estado de tipeo
                        //set data
                        nametv.setText(name);
                        try {
                            Picasso.get().load(hisImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(profileIv);
                        } catch (Exception ex) {
                            Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(profileIv);
                        }
                    }
                    if (type.equals("banda")) {
                        String name = "" + ds.child("nameband").getValue();
                        hisImage = "" + ds.child("profilepic").getValue();
                        //Revisar el estado de tipeo
                        //set data
                        nametv.setText(name);
                        if (hisImage.equals("")) {
                            Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(profileIv);

                        } else {
                            Picasso.get().load(hisImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(profileIv);
                        }

                    }

                    String onlineStatus = "" + ds.child("onlineStatus").getValue();
                    if (onlineStatus.equals("online")) {
                        userStatusTv.setText(onlineStatus);
                    } else {
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(Long.parseLong(onlineStatus));
                        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
                        userStatusTv.setText("Ultima vez: " + dateTime);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sendBtn.setOnClickListener(v -> {
            String mensaje = messageEt.getText().toString().trim();
            if (TextUtils.isEmpty(mensaje)) {
                //texto ingresado
                Toast.makeText(ChatActivity.this, "No se puede enviar el mensaje", Toast.LENGTH_SHORT).show();
            } else {

                sendMensaje(mensaje);
            }
        });
        readMessages();
        seenMessages();
        //check edit text


    }

    private void seenMessages() {
        userRefforSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefforSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceptor().equals(myUid) && chat.getEmisor().equals(hisUid)) {
                        HashMap<String, Object> hashSeenMap = new HashMap<>();
                        hashSeenMap.put("isSeen", true);
                        ds.getRef().updateChildren(hashSeenMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceptor().equals(myUid) && chat.getEmisor().equals(hisUid)
                            || chat.getReceptor().equals(hisUid) && chat.getEmisor().equals(myUid)) {
                        chatList.add(chat);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this, chatList, hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendMensaje(String mensaje) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("emisor", myUid);
        hashMap.put("receptor", hisUid);
        hashMap.put("mensaje", mensaje);
        hashMap.put("timeStamp", timeStamp);
        hashMap.put("isSeen", false);
        databaseReference.child("Chats").push().setValue(hashMap);

        //reset editText despues de enviar el mensaje
        messageEt.setText("");
        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatLista").child(myUid).child(hisUid);

        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef1.child("id").setValue(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatLista").child(hisUid).child(myUid);
        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef2.child("id").setValue(myUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkOnlineStatus(String status) {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        //estado del usuario
        dbref.updateChildren(hashMap);

    }


    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //user is signed in stay here
            myUid = user.getUid(); //mi Uid
        } else {
            //user is signed in stay here
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }


    @Override
    protected void onStart() {
        checkUserStatus();
        //online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //timeStamp
        String timeStamp = String.valueOf(System.currentTimeMillis());
        //offline
        checkOnlineStatus(timeStamp);
        userRefforSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        //online
        checkOnlineStatus("online");
        super.onResume();
    }
}