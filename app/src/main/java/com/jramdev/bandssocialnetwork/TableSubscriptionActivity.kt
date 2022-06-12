package com.jramdev.bandssocialnetwork

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TableSubscriptionActivity : AppCompatActivity() {
    private lateinit var option1: CheckBox
    private lateinit var option2: CheckBox
    private lateinit var option3: CheckBox
    private lateinit var optionfree: TextView
    private lateinit var backButton: ImageButton
    private lateinit var layout1: View
    private lateinit var btnsiguiente: ImageButton
    private lateinit var builder: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_subscription)
        option1 = findViewById(R.id.checkbox1)
        option2 = findViewById(R.id.checkbox2)
        option3 = findViewById(R.id.checkbox3)
        optionfree = findViewById(R.id.optionfree)
        btnsiguiente = findViewById(R.id.buttonSiguiente)
        backButton = findViewById(R.id.backbtn);
        builder = AlertDialog.Builder(this)
        layout1 = LayoutInflater.from(this).inflate(
            R.layout.custom_toast,
            findViewById<ViewGroup>(R.id.custom)
        )
        backButton.setOnClickListener {
            onBackPressed()

        }
        option1.setOnCheckedChangeListener { compoundButton, b ->
            if (option1.isChecked) {
                option2.isChecked = false
                option3.isChecked = false
            }
        }

        option2.setOnCheckedChangeListener { compoundButton, b ->
            if (option2.isChecked) {
                option3.isChecked = false
                option1.isChecked = false
            }
        }
        option3.setOnCheckedChangeListener { compoundButton, b ->
            if (option3.isChecked) {
                option2.isChecked = false
                option1.isChecked = false
            }
        }

        optionfree.setOnClickListener {
            val intent = Intent(
                this,
                RegisterStep1Activity::class.java
            )
            startActivity(intent)
            finish()
        }
        btnsiguiente.setOnClickListener {
            if (option1.isChecked || option2.isChecked || option3.isChecked) {
                val intent = Intent(
                    this,
                    RegisterStep1Activity::class.java
                )
                startActivity(intent)
                finish()
            } else {
                customToastVisualizer("Por favor, seleccione una opcion para continuar.")
            }

        }
    }

    private fun customToastVisualizer(textToast: String) {
        val text = layout1.findViewById<View>(R.id.txtinfo) as TextView
        text.text = textToast
        val customToast = Toast(this).also {
            // View and duration has to be set
            it.view = layout1
            it.duration = Toast.LENGTH_LONG
            it.setGravity(Gravity.BOTTOM, 0, 0)
        }
        customToast.show()
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