package com.jramdev.bandssocialnetwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PostsFragment : Fragment() {

    private lateinit var listdistrict: Spinner
    private lateinit var listgender: Spinner
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var imageButton: ImageButton
    private lateinit var imageButton2: ImageButton
    private lateinit var recyclerView: RecyclerView
    public var postList: List<Post>? = null
    private lateinit var adapterPosts: AdapterPosts
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_posts, container, false)
        listdistrict = view.findViewById(R.id.sp1)
        listgender = view.findViewById(R.id.sp2)
        imageButton = view.findViewById(R.id.filtrobtn)
        imageButton2 = view.findViewById(R.id.filtrobtnclose)
        recyclerView = view.findViewById(R.id.postsRecyclerview)
        val adapter1 = ArrayAdapter.createFromResource(
            view.context,
            R.array.Opciones,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        val adapter2 = ArrayAdapter.createFromResource(
            view.context,
            R.array.Genero,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        listdistrict.adapter = adapter1
        listgender.adapter = adapter2
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        //Set layout tp recyclerview
        //Set layout tp recyclerview
        recyclerView.layoutManager = layoutManager


        //init post list


        //init post list


        //init post list
        postList = ArrayList()
        loadPosts(postList as ArrayList<Post>)
        imageButton.setOnClickListener {
            val selectedItem: String = listgender.selectedItem.toString()
            val selecteditem2: String = listdistrict.selectedItem.toString()
            searchPosts(selectedItem, selecteditem2, postList as ArrayList<Post>)

        }
        imageButton2.setOnClickListener {
            listdistrict.setSelection(0)
            listgender.setSelection(0)
            loadPosts(postList as ArrayList<Post>)
        }


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
                    if (post != null) {
                        postList.add(post)
                    }
                    adapterPosts = AdapterPosts(activity, this@PostsFragment.postList)
                    //set adapter to recycler
                    recyclerView.adapter = adapterPosts
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Toast.makeText(getParentFragment().getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
    }

    private fun searchPosts(query: String, query2: String, postList: ArrayList<Post>) {
        val fUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference("Posts")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (ds in dataSnapshot.children) {
                    val usuario: Post = ds.getValue(Post::class.java)!!

                    //obtener toda la data

                    if (usuario.getpDistrict()
                            .equals(query2) && usuario.getpGender()
                            .equals(query)
                    ) {
                        postList.add(usuario)


                        //refresh adapter

                    }

                    adapterPosts = AdapterPosts(context, postList)
                    adapterPosts.notifyDataSetChanged()
                    recyclerView.adapter = adapterPosts

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}