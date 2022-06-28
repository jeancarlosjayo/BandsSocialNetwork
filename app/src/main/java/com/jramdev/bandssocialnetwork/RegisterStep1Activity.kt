package com.jramdev.bandssocialnetwork

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterStep1Activity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var builder: AlertDialog.Builder
    private lateinit var textstepper1: ImageView
    private lateinit var textstepper2: ImageView
    private lateinit var ruc: TextInputEditText
    private lateinit var dni: TextInputEditText
    private lateinit var nombrecompleto: TextInputEditText
    private lateinit var nombrebanda: TextInputEditText
    private lateinit var nombremanager: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var telefono: TextInputEditText
    private lateinit var empresa: TextInputEditText
    private lateinit var distrito: Spinner
    private lateinit var edad: TextInputEditText
    private lateinit var contrasena: TextInputEditText
    private lateinit var contrasena2: TextInputEditText
    private lateinit var btnregistrar: AppCompatButton
    private lateinit var layout1: View
    private lateinit var typeuser: String
    private lateinit var info1: TextView
    private lateinit var info2: TextView
    private lateinit var info3: TextView
    private lateinit var info4: TextView
    private lateinit var info5: TextView
    private lateinit var info6: TextView
    private lateinit var info7: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_step1)
        loadingDialog = LoadingDialog(this)
        backButton = findViewById(R.id.backbtn);
        builder = AlertDialog.Builder(this)
        textstepper1 = findViewById(R.id.info1)
        textstepper2 = findViewById(R.id.info20)
        ruc = findViewById(R.id.edtRUC)
        dni = findViewById(R.id.edtDNI)
        nombrecompleto = findViewById(R.id.edtNombreCompleto)
        nombrebanda = findViewById(R.id.edtNombreBanda)
        nombremanager = findViewById(R.id.edtNombreManager)
        email = findViewById(R.id.edtEmail)
        telefono = findViewById(R.id.edtTelefono)
        empresa = findViewById(R.id.edtNombreEmpresa)
        distrito = findViewById(R.id.edtDistrito)
        edad = findViewById(R.id.edtEdad)
        contrasena = findViewById(R.id.edtContraseña)
        contrasena2 = findViewById(R.id.edtContraseña2)
        btnregistrar = findViewById(R.id.btnRegistrar)
        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.Opciones,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        distrito.adapter = adapter1
        auth = Firebase.auth
        layout1 = LayoutInflater.from(this).inflate(
            R.layout.custom_toast,
            findViewById<ViewGroup>(R.id.custom)
        )
        info1 = findViewById(R.id.info3)//RUC
        info2 = findViewById(R.id.info4)//DNI
        info3 = findViewById(R.id.info44)//Nombre Completo
        info4 = findViewById(R.id.info5)//Nombre banda
        info5 = findViewById(R.id.info6)//Nombre manager
        info6 = findViewById(R.id.info9)//empresa
        info7 = findViewById(R.id.info10)//edad
        val prefs = getSharedPreferences("credenciales", MODE_PRIVATE)
        typeuser = prefs.getString("usertype", "No name defined").toString()

        if (typeuser == "banda") {
            textstepper1.visibility = View.VISIBLE
            textstepper2.visibility = View.INVISIBLE
            dni.visibility = View.GONE
            info2.visibility = View.GONE
            nombrecompleto.visibility = View.GONE
            info3.visibility = View.GONE
            empresa.visibility = View.GONE
            info6.visibility = View.GONE
            edad.visibility = View.GONE
            info7.visibility = View.GONE
        }
        if (typeuser == "cliente") {
            textstepper1.visibility = View.VISIBLE
            textstepper2.visibility = View.INVISIBLE
            ruc.visibility = View.GONE
            info1.visibility = View.GONE
            nombremanager.visibility = View.GONE
            info4.visibility = View.GONE
            nombrebanda.visibility = View.GONE
            info5.visibility = View.GONE
            empresa.visibility = View.GONE
            info6.visibility = View.GONE
        }
        if (typeuser == "promotor") {
            nombremanager.visibility = View.GONE
            info4.visibility = View.GONE
            textstepper1.visibility = View.INVISIBLE
            textstepper2.visibility = View.VISIBLE
            nombrebanda.visibility = View.GONE
            info5.visibility = View.GONE
        }
        backButton.setOnClickListener {
            onBackPressed()

        }
        btnregistrar.setOnClickListener {
            loadingDialog.startloadingAlertDialog()
            val textRuc: String = ruc.text.toString().trim()
            val textDni: String = dni.text.toString().trim()
            val textNombreCompleto: String = nombrecompleto.text.toString().trim()
            val textNombreBanda: String = nombrebanda.text.toString().trim()
            val textNombreManager: String = nombremanager.text.toString().trim()
            val textEmail: String = email.text.toString().trim()
            val textTelefono: String = telefono.text.toString().trim()
            val textEmpresa: String = empresa.text.toString().trim()
            val textDistrito: String = distrito.selectedItem.toString()
            val textEdad: String = edad.text.toString().trim()
            val textContrasena: String = contrasena.text.toString().trim()
            val textContrasena2: String = contrasena2.text.toString().trim()
            when (typeuser) {
                "banda" ->
                    if (textRuc.isNotEmpty()
                        && textNombreBanda.isNotEmpty() && textNombreManager.isNotEmpty() && textEmail.isNotEmpty()
                        && textTelefono.isNotEmpty() && textContrasena.isNotEmpty() &&
                        textDistrito.isNotEmpty() && textContrasena2.isNotEmpty()
                    ) {
                        if (validateRuc(textRuc)) {
                            if (textEmail.contains("@gmail.com") || textEmail.contains("@outlook.com") || textEmail.contains(
                                    "@yahoo.com"
                                )
                            ) {
                                if (textContrasena == textContrasena2) {
                                    if (isValidPassword(textContrasena) && isValidPassword(
                                            textContrasena2
                                        )
                                    ) {
                                        auth.createUserWithEmailAndPassword(
                                            textEmail,
                                            textContrasena
                                        )
                                            .addOnCompleteListener(this) { task ->
                                                if (task.isSuccessful) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    val user = auth.currentUser
                                                    val hashMap3 = HashMap<Any, String>()
                                                    hashMap3["ruc"] = "" + textRuc
                                                    hashMap3["nameband"] = "" + textNombreBanda
                                                    hashMap3["namemanager"] = "" + textNombreManager
                                                    hashMap3["email"] = "" + textEmail
                                                    hashMap3["phone"] = "" + textTelefono
                                                    hashMap3["district"] = "" + textDistrito
                                                    hashMap3["typeuser"] = "banda"
                                                    if (user != null) {
                                                        registerUser(hashMap3, user)
                                                    }
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    updateUI(null)
                                                }
                                            }
                                    } else {
                                        loadingDialog.dismissLoadingAlertDialog()
                                        customToastVisualizer("La contraseña debe tener minimo 9 caracteres: 1 Minúscula, 1 Mayúscula, 1 Número, 1 Símbolo")
                                    }
                                } else {
                                    loadingDialog.dismissLoadingAlertDialog()
                                    customToastVisualizer("Las contraseñas no son las mismas")
                                }

                            } else {
                                loadingDialog.dismissLoadingAlertDialog()
                                customToastVisualizer("El correo electronico debe tener un dominio valido, @gmail.com, @outlook.com, @yahoo.com")
                            }
                        } else {
                            loadingDialog.dismissLoadingAlertDialog()
                            customToastVisualizer("Ingrese un ruc valido")

                        }
                    } else {
                        loadingDialog.dismissLoadingAlertDialog()
                        customToastVisualizer("Los campos no pueden estar vacios.")
                    }
                "promotor" -> if (textRuc.isNotEmpty() && textDni.isNotEmpty() && textNombreCompleto.isNotEmpty()
                    && textEmail.isNotEmpty() && textTelefono.isNotEmpty() && textEmpresa.isNotEmpty() &&
                    textDistrito.isNotEmpty() && textEdad.isNotEmpty()
                ) {
                    if (validateRuc(textRuc)) {
                        if (textDni.length == 8) {
                            if (Integer.parseInt(textEdad) >= 18) {
                                if (textEmail.contains("@gmail.com") || textEmail.contains("@outlook.com") || textEmail.contains(
                                        "@yahoo.com"
                                    )
                                ) {
                                    if (textContrasena == textContrasena2) {
                                        if (isValidPassword(textContrasena) && isValidPassword(
                                                textContrasena2
                                            )
                                        ) {
                                            auth.createUserWithEmailAndPassword(
                                                textEmail,
                                                textContrasena
                                            )
                                                .addOnCompleteListener(this) { task ->
                                                    if (task.isSuccessful) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        val user = auth.currentUser
                                                        val hashMap3 = HashMap<Any, String>()
                                                        hashMap3["ruc"] = "" + textRuc
                                                        hashMap3["dni"] = "" + textDni
                                                        hashMap3["fullname"] =
                                                            "" + textNombreCompleto
                                                        hashMap3["email"] = "" + textEmail
                                                        hashMap3["phone"] = "" + textTelefono
                                                        hashMap3["district"] = "" + textDistrito
                                                        hashMap3["age"] = "" + textEdad
                                                        hashMap3["enterprise"] = "" + textEmpresa
                                                        hashMap3["typeuser"] = "promotor"
                                                        if (user != null) {
                                                            registerUser(hashMap3, user)
                                                        }
                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        updateUI(null)
                                                    }
                                                }
                                        } else {
                                            loadingDialog.dismissLoadingAlertDialog()
                                            customToastVisualizer("La contraseña debe tener minimo 9 caracteres: 1 Minúscula, 1 Mayúscula, 1 Número, 1 Símbolo")
                                        }
                                    } else {
                                        loadingDialog.dismissLoadingAlertDialog()
                                        customToastVisualizer("Las contraseñas no son las mismas")
                                    }

                                } else {
                                    loadingDialog.dismissLoadingAlertDialog()
                                    customToastVisualizer("El correo electronico debe tener un dominio valido, @gmail.com, @outlook.com, @yahoo.com")
                                }
                            } else {
                                loadingDialog.dismissLoadingAlertDialog()
                                customToastVisualizer("La edad minima es de 18 años")
                            }

                        } else {
                            loadingDialog.dismissLoadingAlertDialog()
                            customToastVisualizer("El DNI ingresado no tiene 8 digitos.")
                        }

                    } else {
                        loadingDialog.dismissLoadingAlertDialog()
                        customToastVisualizer("El RUC ingresado es invalido.")
                    }
                } else {
                    loadingDialog.dismissLoadingAlertDialog()
                    customToastVisualizer("Los campos no pueden estar vacios.")
                }
                "cliente" -> if (textDni.isNotEmpty() && textNombreCompleto.isNotEmpty()
                    && textEmail.isNotEmpty() && textTelefono.isNotEmpty() &&
                    textDistrito.isNotEmpty() && textEdad.isNotEmpty()
                ) {
                    if (textDni.length == 8) {
                        if (Integer.parseInt(textEdad) >= 18) {
                            if (textEmail.contains("@gmail.com") || textEmail.contains("@outlook.com") || textEmail.contains(
                                    "@yahoo.com"
                                )
                            ) {
                                if (textContrasena == textContrasena2) {
                                    if (isValidPassword(textContrasena) && isValidPassword(
                                            textContrasena2
                                        )
                                    ) {
                                        auth.createUserWithEmailAndPassword(
                                            textEmail,
                                            textContrasena
                                        )
                                            .addOnCompleteListener(this) { task ->
                                                if (task.isSuccessful) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    val user = auth.currentUser
                                                    val hashMap3 = HashMap<Any, String>()
                                                    hashMap3["dni"] = "" + textDni
                                                    hashMap3["fullname"] =
                                                        "" + textNombreCompleto
                                                    hashMap3["email"] = "" + textEmail
                                                    hashMap3["phone"] = "" + textTelefono
                                                    hashMap3["district"] = "" + textDistrito
                                                    hashMap3["age"] = "" + textEdad
                                                    hashMap3["typeuser"] = "cliente"
                                                    if (user != null) {
                                                        registerUser(hashMap3, user)
                                                    }
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    updateUI(null)
                                                }
                                            }
                                    } else {
                                        loadingDialog.dismissLoadingAlertDialog()
                                        customToastVisualizer("La contraseña debe tener minimo 9 caracteres: 1 Minúscula, 1 Mayúscula, 1 Número, 1 Símbolo")
                                    }
                                } else {
                                    loadingDialog.dismissLoadingAlertDialog()
                                    customToastVisualizer("Las contraseñas no son las mismas")
                                }

                            } else {
                                loadingDialog.dismissLoadingAlertDialog()
                                customToastVisualizer("El correo electronico debe tener un dominio valido, @gmail.com, @outlook.com, @yahoo.com")
                            }
                        } else {
                            loadingDialog.dismissLoadingAlertDialog()
                            customToastVisualizer("La edad minima es de 18 años")
                        }

                    } else {
                        loadingDialog.dismissLoadingAlertDialog()
                        customToastVisualizer("El DNI ingresado no tiene 8 digitos.")
                    }

                } else {
                    loadingDialog.dismissLoadingAlertDialog()
                    customToastVisualizer("Los campos no pueden estar vacios.")
                }
                else -> {
                    loadingDialog.dismissLoadingAlertDialog()
                    customToastVisualizer("Registro fallido")
                }

            }


        }
    }

    private fun registerUser(hashMap3: HashMap<Any, String>, user: FirebaseUser) {
        val databaseReference2 =
            FirebaseDatabase.getInstance().getReference("users").child(user.uid)
        databaseReference2.setValue(hashMap3).addOnCompleteListener {
            updateUI(user)
        }.addOnFailureListener {
            loadingDialog.dismissLoadingAlertDialog()
            customToastVisualizer("Datos no agregados")
        }
    }


    private fun validateRuc(ruc: String): Boolean {
        val foo: CharSequence = ruc
        val bar = foo.toString()
        val desiredString = bar.substring(0, 2)
        return desiredString == "20" && ruc.length == 11 || desiredString == "10" && ruc.length == 11

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

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            loadingDialog.dismissLoadingAlertDialog()
            val intent = Intent(this, MainActivity::class.java)
            val text = "Registro exitoso"
            customToastVisualizer(text)
            startActivity(intent)
            finish()
        } else {
            loadingDialog.dismissLoadingAlertDialog()
            val text = "Autenticacion fallida"
            customToastVisualizer(text)
        }
    }


}