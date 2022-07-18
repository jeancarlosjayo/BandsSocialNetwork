package com.jramdev.bandssocialnetwork

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class UserSelectionActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var banda: ImageView
    private lateinit var promotor: ImageView
    private lateinit var cliente: ImageView
    private lateinit var builder: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_selection)
        backButton = findViewById(R.id.backbtn);
        builder = AlertDialog.Builder(this)
        banda = findViewById(R.id.banda)
        promotor = findViewById(R.id.promotor)
        cliente = findViewById(R.id.clientedeeventos)
        backButton.setOnClickListener {
            onBackPressed()

        }
        banda.setOnClickListener {
            setTypeofUser("banda")
            val intent = Intent(
                this,
                RegisterActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        promotor.setOnClickListener {
            setTypeofUser("promotor")
            val intent = Intent(
                this,
                TableSubscriptionActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        cliente.setOnClickListener {
            setTypeofUser("cliente")
            val intent = Intent(
                this,
                RegisterActivity::class.java
            )
            startActivity(intent)
            finish()
        }

    }

    private fun setTypeofUser(type: String) {
        val preferences = getSharedPreferences("credenciales", MODE_PRIVATE)
        val editor1 = preferences.edit()
        editor1.putString("usertype", type)
        editor1.apply()
    }

    override fun onBackPressed() {
        builder.setTitle("Advertencia")
        builder.setMessage("¿Desea cancelar el registro?")
        builder.setPositiveButton(
            "Si"
        ) { dialogInterface: DialogInterface?, i: Int -> finish() }
        builder.setNegativeButton(
            "No"
        ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        builder.create()
        builder.show()

    }
}