package com.jramdev.bandssocialnetwork

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AnotherProfileBandaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    public var postList: List<Post>? = null
    private lateinit var adapterPosts: AdapterPosts
    private lateinit var nameBand: TextView
    private lateinit var rucBand: TextView
    private lateinit var districtBand: TextView
    private lateinit var phoneBand: TextView
    private lateinit var openchatProfileButton: ImageView
    var hisUid: String? = null
    private lateinit var backBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another_profile_banda)
        recyclerView = findViewById(R.id.postsRecyclerview)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        //Set layout tp recyclerview
        //Set layout tp recyclerview
        recyclerView.layoutManager = layoutManager
        nameBand = findViewById(R.id.namePBanda)
        rucBand = findViewById(R.id.rucPBanda)
        districtBand = findViewById(R.id.distritoPBanda)
        phoneBand = findViewById(R.id.celularPBanda)
        openchatProfileButton = findViewById(R.id.mensaje)
        backBtn = findViewById(R.id.backbtn)
        backBtn.setOnClickListener {
            finish()
        }
        //Recoge datos para el usuario de chat
        val intent = intent
        hisUid = intent.getStringExtra("SuUID")
        val query: Query = FirebaseDatabase.getInstance().getReference("users").orderByKey()
            .equalTo(hisUid).limitToLast(1)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    //get data
                    val nameband = "" + ds.child("nameband").value
                    val ruc = "" + ds.child("ruc").value
                    val phone = "" + ds.child("phone").value
                    val district = "" + ds.child("district").value
                    nameBand.text = nameband
                    rucBand.text = "RUC: $ruc"
                    phoneBand.text = "Telefono: $phone"
                    districtBand.text = "$district"

                }
                //set data
            }

            override fun onCancelled(error: DatabaseError) {
                query.removeEventListener(this)
            }
        })

        openchatProfileButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("SuUID", hisUid)
            startActivity(intent)
        }
        postList = ArrayList()
        loadPosts(postList as ArrayList<Post>, hisUid)
    }

    private fun loadPosts(postList: ArrayList<Post>, hisUid: String?) {
        val ref = FirebaseDatabase.getInstance().getReference("Posts")
        //get all data
        //get all data
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (ds in dataSnapshot.children) {
                    val post = ds.getValue(Post::class.java)
                    if (post != null && post.getUid()
                            .equals(hisUid)
                    ) {
                        postList.add(post)
                    }
                    adapterPosts = AdapterPosts(this@AnotherProfileBandaActivity, postList)
                    //set adapter to recycler
                    recyclerView.adapter = adapterPosts
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Toast.makeText(getParentFragment().getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })

    }

}