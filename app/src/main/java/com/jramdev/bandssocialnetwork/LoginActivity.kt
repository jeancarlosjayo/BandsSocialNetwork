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

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: ImageButton
    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt: TextInputEditText
    private lateinit var layout1: View
    private lateinit var registerLink: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        layout1 = LayoutInflater.from(this).inflate(
            R.layout.custom_toast,
            findViewById<ViewGroup>(R.id.custom)
        )
        auth = Firebase.auth
        btnLogin = findViewById(R.id.btnLogin)
        emailEdt = findViewById(R.id.userEditText)
        passwordEdt = findViewById(R.id.passwordEditText)
        registerLink = findViewById(R.id.textViewRegister)
        btnLogin.setOnClickListener {
            val password: String = passwordEdt.text.toString()
            val email: String = emailEdt.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (email.contains("@gmail.com") || email.contains("@outlook.com") || email.contains(
                        "@yahoo.com"
                    )
                ) {

                    if (isValidPassword(password)) {

                        auth.signInWithEmailAndPassword(email, password)
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
                            "La contraseña debe tener minimo 9 caracteres: 1 Minúscula, 1 Mayúscula, 1 Número, 1 Símbolo"
                        customToastVisualizer(text)
                    }
                } else {
                    val text =
                        "El correo electronico debe tener un dominio valido, @gmail.com, @outlook.com, @yahoo.com"
                    customToastVisualizer(text)
                }

            } else {
                val text = "Los campos no deben estar vacios"
                customToastVisualizer(text)
            }


        }
        registerLink.setOnClickListener {
            val intent = Intent(this, UserSelectionActivity::class.java)
            startActivity(intent)

        }
    }

    //public override fun onStart() {
    //  super.onStart()
    // Check if user is signed in (non-null) and update UI accordingly.
    //val currentUser = auth.currentUser
    ///updateUI(currentUser)

    //}


    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            val text = "Autenticacion exitosa"
            customToastVisualizer(text)
            startActivity(intent)
        } else {
            val text = "Usuario y/o contraseña no válido"
            customToastVisualizer(text)
        }
    }

    private fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password!!)
        return matcher.matches()
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