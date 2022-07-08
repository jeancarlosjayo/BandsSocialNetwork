package com.jramdev.bandssocialnetwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfilePromotorFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    public var postList: List<Post>? = null
    private lateinit var adapterPosts: AdapterPosts
    private lateinit var nameBand: TextView
    private lateinit var rucBand: TextView
    private lateinit var districtBand: TextView
    private lateinit var phoneBand: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile_promotor, container, false)
        recyclerView = view.findViewById(R.id.postsRecyclerview)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        //Set layout tp recyclerview
        //Set layout tp recyclerview
        recyclerView.layoutManager = layoutManager
        nameBand = view.findViewById(R.id.namePCliente)
        rucBand = view.findViewById(R.id.phonePCliente)
        val query: Query =
            FirebaseDatabase.getInstance().getReference("users").orderByChild("email")
                .equalTo(FirebaseAuth.getInstance().currentUser?.email).limitToLast(1)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    //get data
                    val nameband = "" + ds.child("fullname").value
                    val ruc = "" + ds.child("ruc").value

                    nameBand.text = nameband
                    rucBand.text = "RUC: $ruc"


                }
                //set data
            }

            override fun onCancelled(error: DatabaseError) {
                query.removeEventListener(this)
            }
        })

        //init post list


        //init post list


        //init post list
        postList = ArrayList()
        loadPosts(postList as ArrayList<Post>)
        return view
    }

    private fun loadPosts(postList: ArrayList<Post>) {
        val ref = FirebaseDatabase.getInstance().getReference("Posts")
        //get all data
        //get all data
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (ds in dataSnapshot.children) {
                    val post = ds.getValue(Post::class.java)
                    if (post != null && post.getUid()
                            .equals(FirebaseAuth.getInstance().currentUser?.uid)
                    ) {
                        postList.add(post)
                    }
                    adapterPosts = AdapterPosts(activity, this@ProfilePromotorFragment.postList)
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