package com.jramdev.bandssocialnetwork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {
    Timer taskdate;
    TimerTask myTask;
    Context context;
    public List<Post> postList;
    AlertDialog.Builder builder;

    public AdapterPosts(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
//get data pId, pTitle, uImage, pImage, pTime, uid, uEmail, pDescription, uName;
        final String uid = postList.get(i).getUid();
        String uEmail = postList.get(i).getuEmail();
        String uName = postList.get(i).getuName();
        String uImage = postList.get(i).getuImage();
        String pId = postList.get(i).getpId();
        String pTitle = postList.get(i).getpTitle();
        String pDescription = postList.get(i).getpDescription();
        String pImage = postList.get(i).getpImage();
        String pTimeStamp = postList.get(i).getpTime();
        String pdistrict = postList.get(i).getpDistrict();
        //convert timestamp
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
//set data
        myHolder.uNameTv.setText(uName);
        myHolder.pTimeTv.setText(pTime);
        myHolder.pTittle.setText(pTitle);
        myHolder.pDescriptiontV.setText(pDescription);
        myHolder.pDistrictTV.setText(pdistrict);
        //set user up
        try {
            Picasso.get().load(uImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(myHolder.uPictureIv);
        } catch (Exception ex) {
            Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(myHolder.uPictureIv);
        }

//set post image
        if (pImage.equals("noImage")) {
//hide imageview
            myHolder.pImageIv.setVisibility(View.GONE);
        } else {
            try {
                Picasso.get().load(pImage).into(myHolder.pImageIv);
            } catch (Exception ex) {
                Picasso.get().load(R.drawable.ic_baseline_image_24).into(myHolder.pImageIv);
            }
        }
        if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            myHolder.moreBtn.setVisibility(View.VISIBLE);
        } else {
            myHolder.moreBtn.setVisibility(View.GONE);
        }

        myHolder.moreBtn.setOnClickListener(v -> showDialog(i, pId));
        myHolder.likeBtn.setOnClickListener(v -> Toast.makeText(context, "Me gusta", Toast.LENGTH_SHORT).show());
        myHolder.commentBtn.setOnClickListener(v -> Toast.makeText(context, "Comenta", Toast.LENGTH_SHORT).show());
        myHolder.uPictureIv.setOnClickListener(v -> {
            if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                Toast.makeText(context, FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            } else {
                Query query = FirebaseDatabase.getInstance().getReference("users").orderByKey().equalTo(uid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        query.removeEventListener(this);
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String type = "" + ds.child("typeuser").getValue().toString();
                            switch (type) {
                                case "cliente":
                                    Intent intent = new Intent(context, AnotherProfileClienteActivity.class);
                                    intent.putExtra("SuUID", uid);
                                    context.startActivity(intent);
                                    break;
                                case "promotor":
                                    Intent intent1 = new Intent(context, AnotherProfilePromotorActivity.class);
                                    intent1.putExtra("SuUID", uid);
                                    context.startActivity(intent1);
                                    break;
                                case "banda":
                                    Intent intent2 = new Intent(context, AnotherProfileBandaActivity.class);
                                    intent2.putExtra("SuUID", uid);
                                    context.startActivity(intent2);
                                    break;
                                default:
                                    Toast.makeText(context, "No se ha podido encontrar el perfil o la publicacion se ha eliminado.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        query.removeEventListener(this);
                    }
                });
            }


        });

    }

    //Iniciar tareas automaticas
    private void inittask(MyHolder myHolder, String pId) {
        taskdate = new Timer();
        myTask = new TimerTask() {
            @Override
            public void run() {
                // your code
                //taskLikeTextRefresh(myHolder, pId);
            }
        };

        taskdate.scheduleAtFixedRate(myTask, 0l, 5000); // Runs every 5 mins
    }

    private void taskLikeTextRefresh(MyHolder myHolder, String pId) {

        Query query1 = FirebaseDatabase.getInstance().getReference("Reactions").child(pId);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String count = "" + ds.child("count").getValue();
                        myHolder.pLikesTv.setText(count);
                    }
                } else {
                    myHolder.pLikesTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void likeProccess(int i, MyHolder myHolder, String pId) {
        SharedPreferences prefs = context.getSharedPreferences(
                "countlikebyuser", Context.MODE_PRIVATE);
        String text = prefs.getString(pId, null);
        if (text == "yes") {
            Query query = FirebaseDatabase.getInstance().getReference("Reactions").child(pId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String count = "" + ds.child("count").getValue();
                            int prevval = Integer.parseInt(count);
                            int nextval = prevval - 1;
                            String countlast = String.valueOf(nextval);
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //insertar mapa hash
                            hashMap.put("count", countlast);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reactions");
                            databaseReference.child(pId).setValue(hashMap).addOnSuccessListener(unused -> {
                                SharedPreferences prefs = context.getSharedPreferences(
                                        "countlikebyuser", Context.MODE_PRIVATE);
                                prefs.edit().putString(pId, "yes").apply();
                                query.removeEventListener(this);
                            }).addOnFailureListener(e -> {
                                SharedPreferences prefs = context.getSharedPreferences(
                                        "countlikebyuser", Context.MODE_PRIVATE);
                                prefs.edit().putString(pId, "no").apply();
                                query.removeEventListener(this);
                            });
                        }
                    } else {
                        HashMap<Object, String> hashMap = new HashMap<>();
                        //insertar mapa hash
                        hashMap.put("count", "1");
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reactions");
                        databaseReference.child(pId).setValue(hashMap).addOnSuccessListener(unused -> {
                            SharedPreferences prefs = context.getSharedPreferences(
                                    "countlikebyuser", Context.MODE_PRIVATE);
                            prefs.edit().putString(pId, "yes").apply();
                            query.removeEventListener(this);
                        }).addOnFailureListener(e -> {
                            SharedPreferences prefs = context.getSharedPreferences(
                                    "countlikebyuser", Context.MODE_PRIVATE);
                            prefs.edit().putString(pId, "no").apply();
                            query.removeEventListener(this);
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            if(text=="no"){
                Query query = FirebaseDatabase.getInstance().getReference("Reactions").child(pId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String count = "" + ds.child("count").getValue();
                                int prevval = Integer.parseInt(count);
                                int nextval = prevval + 1;
                                String countlast = String.valueOf(nextval);
                                HashMap<Object, String> hashMap = new HashMap<>();
                                //insertar mapa hash
                                hashMap.put("count", countlast);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reactions");
                                databaseReference.child(pId).setValue(hashMap).addOnSuccessListener(unused -> {
                                    SharedPreferences prefs = context.getSharedPreferences(
                                            "countlikebyuser", Context.MODE_PRIVATE);
                                    prefs.edit().putString(pId, "yes").apply();
                                    query.removeEventListener(this);
                                }).addOnFailureListener(e -> {
                                    SharedPreferences prefs = context.getSharedPreferences(
                                            "countlikebyuser", Context.MODE_PRIVATE);
                                    prefs.edit().putString(pId, "no").apply();
                                    query.removeEventListener(this);
                                });
                            }
                        } else {
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //insertar mapa hash
                            hashMap.put("count", "1");
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reactions");
                            databaseReference.child(pId).setValue(hashMap).addOnSuccessListener(unused -> {
                                SharedPreferences prefs = context.getSharedPreferences(
                                        "countlikebyuser", Context.MODE_PRIVATE);
                                prefs.edit().putString(pId, "yes").apply();
                                query.removeEventListener(this);
                            }).addOnFailureListener(e -> {
                                SharedPreferences prefs = context.getSharedPreferences(
                                        "countlikebyuser", Context.MODE_PRIVATE);
                                prefs.edit().putString(pId, "no").apply();
                                query.removeEventListener(this);
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }


    }

    private void showDialog(int i, String pId) {
        //options(camera,gallery) to show in dialog
        String[] options = {"Modificar", "Eliminar"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Opciones: ");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {//camera click
                Intent intent = new Intent(context, UpdatePublicationActivity.class);
                intent.putExtra("IdPost", pId);
                context.startActivity(intent);

            }
            if (which == 1) {
                postList.remove(i);
                Toast.makeText(
                        context,
                        "Opcion eliminar",
                        Toast.LENGTH_SHORT
                ).show();

            }
        });
        //crear y mirar dialog
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder {
        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pTittle, pDescriptiontV, pLikesTv, pDistrictTV, countLikes;
        ImageButton moreBtn;
        ImageButton likeBtn, commentBtn;
        LinearLayout profileLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            uPictureIv = itemView.findViewById(R.id.pProfilepic);
            pImageIv = itemView.findViewById(R.id.pImageIv);
            uNameTv = itemView.findViewById(R.id.uNameiv);
            pTimeTv = itemView.findViewById(R.id.pTimetv);
            pTittle = itemView.findViewById(R.id.pTittleTv);
            pDescriptiontV = itemView.findViewById(R.id.pDescripcionTv);
            pLikesTv = itemView.findViewById(R.id.pLikeTv);
            moreBtn = itemView.findViewById(R.id.moreButton);
            likeBtn = itemView.findViewById(R.id.likeB);
            commentBtn = itemView.findViewById(R.id.commentB);
            profileLayout = itemView.findViewById(R.id.profileLayout);
            pDistrictTV = itemView.findViewById(R.id.pdistrictTV);

        }
    }
}
