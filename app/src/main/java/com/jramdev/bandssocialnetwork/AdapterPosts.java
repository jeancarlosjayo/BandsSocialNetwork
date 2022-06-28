package com.jramdev.bandssocialnetwork;

import android.content.Context;
import android.content.DialogInterface;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

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
            Picasso.get().load(uImage).placeholder(R.drawable.ic_baseline_image_24).into(myHolder.uPictureIv);
        } catch (Exception ex) {

        }

//set post image
        if (pImage.equals("noImage")) {
//hide imageview
            myHolder.pImageIv.setVisibility(View.GONE);
        } else {
            try {
                Picasso.get().load(pImage).into(myHolder.pImageIv);
            } catch (Exception ex) {

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
        myHolder.profileLayout.setOnClickListener(v -> {
            // Intent intent=new Intent(context, OtroPerfilActivity.class);
            // intent.putExtra("uid",uid);
            // context.startActivity(intent);

        });

    }

    private void showDialog(int i, String pId) {
        //options(camera,gallery) to show in dialog
        String[] options = {"Modificar", "Eliminar"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Opciones: ");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {//camera click
                    Toast.makeText(
                            context,
                            "Opcion modificar",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                if (which == 1) {
                    postList.remove(i);
                    Toast.makeText(
                            context,
                            "Opcion eliminar",
                            Toast.LENGTH_SHORT
                    ).show();

                }
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
        TextView uNameTv, pTimeTv, pTittle, pDescriptiontV, pLikesTv, pDistrictTV;
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
