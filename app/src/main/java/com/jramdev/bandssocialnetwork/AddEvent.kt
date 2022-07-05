package com.jramdev.bandssocialnetwork

import android.Manifest
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddEvent : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var backButton: ImageButton
    private lateinit var addEvent: ImageButton
    private lateinit var builder: AlertDialog.Builder
    private lateinit var edtTituloEvento: TextInputEditText
    private lateinit var edtDescripcion: TextInputEditText
    private lateinit var pImageText: TextView
    private lateinit var pImage: ImageView
    private lateinit var edtFecha: TextInputEditText
    private var firebaseAuth: FirebaseAuth? = null
    private var userDatabase: DatabaseReference? = null
    private var storageReference: StorageReference? = null
    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 200
    private val IMAGE_PICK_CAMERA_CODE = 300
    private val IMAGE_PICK_GALLERY_CODE = 400
    var image_uri: Uri? = null
    var nombre: String? = null
    var email: String? = null
    var uid: String? = null
    var imagen: String? = null
    var distrito: String? = null

    //PERMISSIONS ARRAY
    lateinit var cameraPermissions: Array<String>
    lateinit var storagePermissions: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_evento)
        loadingDialog = LoadingDialog(this)
//init Permissions arrays
        cameraPermissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference //firebase storage reference

        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        val query: Query = userDatabase!!.orderByChild("email")
            .equalTo(FirebaseAuth.getInstance().currentUser?.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val type = "" + ds.child("typeuser").value
                    if (type == "banda") {
                        query.removeEventListener(this)
                        nombre = "" + ds.child("nameband").value
                        email = "" + ds.child("email").value
                        imagen = "" + ds.child("image").value
                        distrito = "" + ds.child("district").value
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        //get image from camera/gallery on click
        backButton = findViewById(R.id.backbtn);
        edtTituloEvento = findViewById(R.id.edtTituloEvento)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        pImageText = findViewById(R.id.pImageText)
        pImage = findViewById(R.id.pImage)
        edtFecha = findViewById(R.id.edtFecha)
        addEvent = findViewById(R.id.addEvento)
        pImage.setOnClickListener { showImagePickDialog() }


        val adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.Genero,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        builder = AlertDialog.Builder(this)
        backButton.setOnClickListener { onBackPressed() }
        addEvent.setOnClickListener {
            val title: String = edtTituloEvento.text.toString().trim { it <= ' ' }
            val description: String = edtDescripcion.text.toString().trim { it <= ' ' }
            val dateEvent: String = edtFecha.text.toString().trim { it <= ' ' }
            if (title.isEmpty()) {
                Toast.makeText(this, "Ingrese titulo", Toast.LENGTH_SHORT).show()
            } else {
                if (description.isEmpty()) {
                    Toast.makeText(this, "Ingrese descripcion", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (dateEvent.isEmpty()) {
                        Toast.makeText(this, "Ingrese fecha", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (image_uri == null) {
                            //post without image
                            uploadData( title, description, "noImage", dateEvent)
                        } else {
                            uploadData( title, description, image_uri.toString(), dateEvent)
                        }
                    }
                }
            }


        }

    }

    private fun uploadData(title: String, description: String, uri: String, dateEvent: String) {
        loadingDialog.startloadingAlertDialog()
        val timeStamp = System.currentTimeMillis().toString()
        val filePathAndName = "Posts/post_$timeStamp"
        if (uri != "noImage") {
            //post with image
            val rf = FirebaseStorage.getInstance().reference.child(filePathAndName)
            rf.putFile(Uri.parse(uri)).addOnSuccessListener { taskSnapshot ->
                //image
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val downloadUri = uriTask.result.toString()
                if (uriTask.isSuccessful) {
                    //uri //get data pId, pTitle, uImage, pImage, pTime, uid, uEmail, pDescription, uName;
                    val hashMap = HashMap<Any, String>()
                    hashMap["uid"] = uid!!
                    hashMap["uName"] = nombre!!
                    hashMap["uEmail"] = email!!
                    hashMap["uImage"] = imagen!!
                    hashMap["pId"] = timeStamp
                    hashMap["pTitle"] = title
                    hashMap["pDescription"] = description
                    hashMap["pImage"] = downloadUri
                    hashMap["pFecha"] = dateEvent
                    hashMap["pTime"] = timeStamp
                    hashMap["pDistrict"] = distrito!!
                    val ref = FirebaseDatabase.getInstance().getReference("Posts")
                    //put data in this  ref
                    ref.child(timeStamp).setValue(hashMap)
                        .addOnSuccessListener {
                            loadingDialog.dismissLoadingAlertDialog()
                            Toast.makeText(
                                this,
                                "Evento publicado",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            //reset views
                            edtTituloEvento.setText("")
                            edtDescripcion.setText("")
                            pImage.setImageURI(null)
                            edtFecha.setText("")
                            image_uri = null
                            finish()
                        }
                        .addOnFailureListener { e ->
                            loadingDialog.dismissLoadingAlertDialog()
                            Toast.makeText(
                                this,
                                "Ocurrio un errror en la publicacion" + e.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                }
            }
                .addOnFailureListener { e ->
                    loadingDialog.dismissLoadingAlertDialog()
                    Toast.makeText(
                        this,
                        "Ocurrio un errror en la publicacion" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
//post without image
            val hashMap = HashMap<Any, String>()
            hashMap["uid"] = uid!!
            hashMap["uName"] = nombre!!
            hashMap["uEmail"] = email!!
            hashMap["uImage"] = imagen!!
            hashMap["pId"] = timeStamp
            hashMap["pTitle"] = title
            hashMap["pDescription"] = description
            hashMap["pImage"] = "noImage"
            hashMap["pFecha"] = dateEvent
            hashMap["pTime"] = timeStamp
            hashMap["pDistrict"] = distrito!!
            val ref = FirebaseDatabase.getInstance().getReference("Posts")
            //put data in this  ref
            ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener {
                    loadingDialog.dismissLoadingAlertDialog()
                    Toast.makeText(this, "Post publicado", Toast.LENGTH_SHORT)
                        .show()
                    edtTituloEvento.setText("")
                    edtDescripcion.setText("")
                    pImage.setImageURI(null)
                    edtFecha.setText("")
                    image_uri = null
                    finish()
                }
                .addOnFailureListener { e ->
                    loadingDialog.dismissLoadingAlertDialog()
                    Toast.makeText(
                        this,
                        "Ocurrio un errror en la publicacion" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun showImagePickDialog() {
        //options(camera,gallery) to show in dialog

        //options(camera,gallery) to show in dialog
        val options = arrayOf("Camara", "Galeria")
        //dialog
        //dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Escoge una imagen de: ")
        builder.setItems(options) { dialog, which ->
            if (which == 0) { //camera click
                if (!checkCameraPermission()) {
                    requestCameraPermissions()
                } else {
                    PickFromCamera()
                }
            }
            if (which == 1) {
                if (!checkStoragePermission()) {
                    requesStoragePermissions()
                } else {
                    PickFromGallery()
                }
            }
        }
        builder.create().show()
    }

    private fun PickFromGallery() {
//intent to pick image from Gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun PickFromCamera() {
        val cv = ContentValues()
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick")
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(
            intent,
            IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requesStoragePermissions() {
        ActivityCompat.requestPermissions(
            this,
            storagePermissions,
            STORAGE_REQUEST_CODE
        )
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun requestCameraPermissions() {
        ActivityCompat.requestPermissions(
            this,
            cameraPermissions,
            CAMERA_REQUEST_CODE
        )
    }

    override fun onBackPressed() {
        builder.setTitle("Advertencia")
        builder.setMessage("¿Desea cancelar la publicacion?")
        builder.setPositiveButton(
            "Si"
        ) { dialogInterface: DialogInterface?, i: Int -> finish() }
        builder.setNegativeButton(
            "No"
        ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        builder.create()
        builder.show()

    }

    override fun onStart() {
        super.onStart()
        checkUserStatus()
    }

    private fun checkUserStatus() {
        val user = firebaseAuth!!.currentUser
        if (user != null) {
            //user is signed in stay here
            email = user.email
            uid = user.uid
        } else {
            //user is signed in stay here
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted) {
                        PickFromCamera()
                    } else {
                        //camera or gallery or both
                        Toast.makeText(
                            this,
                            "Los permisos de Camara y Almacenamiento son necesarios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        PickFromGallery()
                    } else {
                        //camera or gallery or both
                        Toast.makeText(
                            this,
                            "Los permisos Almacenamiento son necesarios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //este metodo sera llamado despues de seleccionar la foto
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data!!.data
                //set to imageview
                pImage.setImageURI(image_uri)
                pImageText.text = image_uri.toString()
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                pImage.setImageURI(image_uri)
                pImageText.text = image_uri.toString()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}