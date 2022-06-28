package com.jramdev.bandssocialnetwork

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var signoutBtn: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var navigationBarView: NavigationBarView
    private lateinit var linearLayout1: LinearLayout
    private lateinit var linearLayout2: LinearLayout
    private lateinit var addPostButton: ImageButton
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationBarView = findViewById(R.id.bottomNavigation)
        navigationBarView.setOnItemSelectedListener(selectedListener)
        builder = AlertDialog.Builder(this)
        linearLayout1 = findViewById(R.id.linearLayout1)
        linearLayout2 = findViewById(R.id.linearLayout2)
        addPostButton = findViewById(R.id.imgButton)
        linearLayout1.visibility = View.VISIBLE
        linearLayout2.visibility = View.INVISIBLE
        val user = FirebaseAuth.getInstance().currentUser
        updateUI(user)
        signoutBtn = findViewById(R.id.backButton)
        signoutBtn.setOnClickListener {
            builder.setTitle("Advertencia")
            builder.setMessage("¿Desea cerrar sesión?")
            builder.setCancelable(false)
            builder.setPositiveButton(
                "Si"
            ) { dialogInterface: DialogInterface?, i: Int ->
                FirebaseAuth.getInstance().signOut()
                val user = FirebaseAuth.getInstance().currentUser
                updateUI(user)
            }
            builder.setNegativeButton(
                "No"
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            builder.create()
            builder.show()


        }
        addPostButton.setOnClickListener {
            val intent = Intent(this, AddPublicationActivity::class.java)
            startActivity(intent)
        }
        val fragment1 = PostsFragment()
        val ft1 = supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
        )
        ft1.replace(R.id.content, fragment1, "")
        ft1.commit()

    }

    private val selectedListener =
        NavigationBarView.OnItemSelectedListener { menuItem -> //handle
            when (menuItem.itemId) {
                R.id.idPublications -> {
                    //profile fragment transaction
                    val fragment2 =
                        PostsFragment()
                    val ft2 = supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                    )
                    ft2.replace(R.id.content, fragment2, "")
                    ft2.commit()
                    return@OnItemSelectedListener true
                }
                R.id.idSearch -> {
                    //home fragment transaction
                    val fragment1 =
                        SearchFragment()
                    val ft1 = supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                    )
                    ft1.replace(R.id.content, fragment1, "")
                    ft1.commit()
                    return@OnItemSelectedListener true
                }
                R.id.idChats -> {
                    //home fragment transaction
                    val fragment1 =
                        ChatsFragment()
                    val ft1 = supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                    )
                    ft1.replace(R.id.content, fragment1, "")
                    ft1.commit()
                    return@OnItemSelectedListener true
                }
                R.id.idCalendar -> {
                    //home fragment transaction
                    val fragment1 =
                        CalendarFragment()
                    val ft1 = supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                    )
                    ft1.replace(R.id.content, fragment1, "")
                    ft1.commit()
                    return@OnItemSelectedListener true
                }
                R.id.idProfile -> {
                    //home fragment transaction
                    val fragment1 =
                        ProfileFragment()
                    val ft1 = supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                    )
                    ft1.replace(R.id.content, fragment1, "")
                    ft1.commit()
                    return@OnItemSelectedListener true
                }


            }
            false
        }

    private fun loadingUserMenu(currentUser: FirebaseUser) {
        val menuBar = navigationBarView.menu
        val query: Query =
            FirebaseDatabase.getInstance().getReference("users").orderByChild("email")
                .equalTo(currentUser.email).limitToLast(1)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    //get data
                    val type = "" + ds.child("typeuser").value
                    if (type == "banda") {
                        query.removeEventListener(this)
                        menuBar.getItem(3).isVisible = false
                        linearLayout1.visibility = View.INVISIBLE
                        linearLayout2.visibility = View.VISIBLE
                    }
                    if (type == "promotor") {
                        query.removeEventListener(this)
                        menuBar.getItem(3).isVisible = false
                        linearLayout1.visibility = View.INVISIBLE
                        linearLayout2.visibility = View.VISIBLE
                    }
                    if (type == "cliente") {
                        query.removeEventListener(this)
                        linearLayout1.visibility = View.INVISIBLE
                        linearLayout2.visibility = View.VISIBLE
                    }

                }
                //set data
            }

            override fun onCancelled(error: DatabaseError) {
                query.removeEventListener(this)
            }
        })

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            loadingUserMenu(currentUser)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
