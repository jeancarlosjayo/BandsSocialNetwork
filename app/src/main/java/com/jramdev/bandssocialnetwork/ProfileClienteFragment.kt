package com.jramdev.bandssocialnetwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileClienteFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    public var postList: List<Post>? = null
    private lateinit var adapterPosts: AdapterPosts
    private lateinit var nameBand: TextView
    private lateinit var rucBand: TextView
    private lateinit var districtBand: TextView
    private lateinit var phoneBand: TextView
    private lateinit var editProfileButton: ImageView
    private lateinit var openchatProfileButton: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile_cliente, container, false)
        recyclerView = view.findViewById(R.id.postsRecyclerview)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        //Set layout tp recyclerview
        //Set layout tp recyclerview
        recyclerView.layoutManager = layoutManager

        nameBand = view.findViewById(R.id.namePCliente)
        phoneBand = view.findViewById(R.id.phonePCliente)
        rucBand = view.findViewById(R.id.edadPCliente)
        districtBand = view.findViewById(R.id.distritPCliente)
        editProfileButton = view.findViewById(R.id.editar)

        val query: Query =
            FirebaseDatabase.getInstance().getReference("users").orderByChild("email")
                .equalTo(FirebaseAuth.getInstance().currentUser?.email).limitToLast(1)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    //get data
                    val nameband = "" + ds.child("fullname").value
                    val ruc = "" + ds.child("age").value
                    val phone = "" + ds.child("phone").value
                    val district = "" + ds.child("district").value
                    nameBand.text = nameband
                    rucBand.text = "Edad: $ruc"
                    phoneBand.text = "Telefono: $phone"
                    districtBand.text = "$district"

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
                    adapterPosts = AdapterPosts(activity, this@ProfileClienteFragment.postList)
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