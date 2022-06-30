package com.jramdev.bandssocialnetwork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern

class PasarelaActivtiy: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnPago: ImageButton
    private lateinit var numTarjeta: EditText
    private lateinit var fecVencimiento: EditText
    private lateinit var ccv: EditText
    private lateinit var nombre: EditText
    private lateinit var correo: EditText

    //private lateinit var passwordEdt: TextInputEditText
    private lateinit var layout1: View
    //private lateinit var registerLink: TextView
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasarela)
        loadingDialog = LoadingDialog(this)
        layout1 = LayoutInflater.from(this).inflate(
            R.layout.custom_toast,
            findViewById<ViewGroup>(R.id.custom)
        )
        auth = Firebase.auth
        btnPago = findViewById(R.id.btnPagar)
        numTarjeta = findViewById(R.id.userEditText)
        fecVencimiento = findViewById(R.id.fechaEditText)
        ccv = findViewById(R.id.cvvEditText)
        nombre = findViewById(R.id.nombreEditText)
        correo = findViewById(R.id.correoEditText)

        numTarjeta.text.clear()
        fecVencimiento.text?.clear()
        ccv.text?.clear()
        nombre.text?.clear()
        correo.text?.clear()

        btnPago.setOnClickListener {
            loadingDialog.startloadingAlertDialog()
            val numtarjeta: String = numTarjeta.text.toString()
            val fecvenc: String = fecVencimiento.text.toString()
            val ccv: String = ccv.text.toString()
            val name: String = nombre.text.toString()
            val email: String = correo.text.toString()

            if (email.isNotEmpty() && numtarjeta.isNotEmpty() && fecvenc.isNotEmpty() && ccv.isNotEmpty() && name.isNotEmpty()) {
                if (email.contains("@gmail.com") || email.contains("@outlook.com") || email.contains(
                        "@yahoo.com"
                    )
                ) {

                    val numtarjetaCount = numtarjeta.length
                    val fecvencCount = fecvenc.length
                    val ccvCount = ccv.length

                    if (ccvCount >= 3) {
                    if (fecvencCount == 7) {
                    if (numtarjetaCount == 16) {

                        auth.signInWithEmailAndPassword(email, numtarjeta)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("", "signInWithEmail:success")
                                    val user = auth.currentUser
                                    updateUI(user)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("", "signInWithEmail:failure", task.exception)
                                    updateUI(null)
                                }
                            }

                    } else {
                        val text =
                            "número de tarjeta inválida, añadir 16 números"
                        loadingDialog.dismissLoadingAlertDialog()
                        customToastVisualizer(text)

                    }
                        } else {
                            val text =
                                "fecha de vencimiento inválida"
                            loadingDialog.dismissLoadingAlertDialog()
                            customToastVisualizer(text)

                        }
                            } else {
                                val text =
                                    "CCV inválida"
                                loadingDialog.dismissLoadingAlertDialog()
                                customToastVisualizer(text)

                            }
                } else {
                    val text =
                        "El correo electronico debe tener un dominio valido, @gmail.com, @outlook.com, @yahoo.com"
                    loadingDialog.dismissLoadingAlertDialog()
                    customToastVisualizer(text)
                }

            } else {
                val text = "Favor de llenar todos los campos"
                loadingDialog.dismissLoadingAlertDialog()
                customToastVisualizer(text)
            }


        }

    }

    private fun isValidNumTarjeta(password: String?): Boolean {
        val pattern: Pattern
        val PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password!!)
        return matcher.matches()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        //if (currentUser != null) {
            val intent = Intent(this, RegisterStep1Activity::class.java)
            val text = "Pago exitosa"
            loadingDialog.dismissLoadingAlertDialog()
            customToastVisualizer(text)
            numTarjeta.text.clear()
            fecVencimiento.text?.clear()
            ccv.text?.clear()
            nombre.text?.clear()
            correo.text?.clear()
            startActivity(intent)
            finish()
//        } else {
//            val text = "No se pudo realizar el pago"
//            loadingDialog.dismissLoadingAlertDialog()
//            customToastVisualizer(text)
//        }
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

}