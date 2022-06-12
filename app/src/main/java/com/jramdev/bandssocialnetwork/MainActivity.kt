package com.jramdev.bandssocialnetwork

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var signoutBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signoutBtn = findViewById(R.id.backButton)
        signoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()

        }
    }
}