package com.jramdev.bandssocialnetwork

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    public lateinit var postList: List<Post>
    private lateinit var adapterPosts: AdapterPosts
    private lateinit var searchBar: SearchView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.postsRecyclerview)
        searchBar = view.findViewById(R.id.searchViewId)
        firebaseAuth = FirebaseAuth.getInstance()
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        //Set layout tp recyclerview
        //Set layout tp recyclerview
        recyclerView.layoutManager = layoutManager
//iniciar lista de usuarios
        //iniciar lista de usuarios
        postList = ArrayList()
//getAll users
        //getAll users
        loadPosts(postList as ArrayList<Post>)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                //llamado  cuando el usuario presiona buscar desde el teclado
                if (!TextUtils.isEmpty(s.trim { it <= ' ' })) {
                    searchPosts(s, postList as ArrayList<Post>)
                } else {
                    loadPosts(postList as ArrayList<Post>)
                }
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (!TextUtils.isEmpty(s.trim { it <= ' ' })) {
                    searchPosts(s, postList as ArrayList<Post>)
                } else {
                    loadPosts(postList as ArrayList<Post>)
                }
                return false
            }
        })
        return view
    }

    private fun loadPosts(postList: ArrayList<Post>) {
        val fUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference("Posts")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (ds in dataSnapshot.children) {
                    val usuario: Post? = ds.getValue(Post::class.java)

                    //obtener toda la data
                    if (usuario != null) {

                            postList.add(usuario)

                    }
                    //Adapter
                    adapterPosts = AdapterPosts(activity, postList)
                    recyclerView.adapter = adapterPosts
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    private fun searchPosts(query: String, postList: ArrayList<Post>) {
        val fUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference("Posts")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (ds in dataSnapshot.children) {
                    val usuario: Post = ds.getValue(Post::class.java)!!

                    //obtener toda la data

                        if (usuario.getuName().toLowerCase()
                                .contains(query.lowercase(Locale.getDefault())) ||
                            usuario.getpDescription().toLowerCase()
                                .contains(query.lowercase(Locale.getDefault())) ||
                            usuario.getpGender().toLowerCase()
                                .contains(query.lowercase(Locale.getDefault())) ||
                            usuario.getpTitle().toLowerCase()
                                .contains(query.lowercase(Locale.getDefault()))
                        ) {
                            postList.add(usuario)
                        }


                    //Adapter
                    adapterPosts =
                        AdapterPosts(context, postList)
                    //refresh adapter
                    adapterPosts.notifyDataSetChanged()
                    recyclerView.adapter = adapterPosts
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}