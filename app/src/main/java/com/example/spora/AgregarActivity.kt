package com.example.spora

import android.app.Activity
import android.content.Intent
import android.icu.text.Normalizer.NO
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.annotation.BoolRes
import androidx.lifecycle.Observer
import com.example.spora.bd.BaseDatos
import com.example.spora.databinding.ActivityAgregarBinding
import com.example.spora.image.ImageController
import com.example.spora.usuario.Usuario
import kotlinx.android.synthetic.main.activity_agregar.*
import kotlinx.android.synthetic.main.lista_usuarios.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class AgregarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarBinding
    private var SELECCIONAR_ACTIVITY = 1002
    private var imagenUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_agregar)

        comprobarErrores()
        btnCancelar.setOnClickListener {
            finish()
        }

        var idUsuario: Int? = null
        if (intent.hasExtra("usuario")){
            val usuario = intent.extras?.getSerializable("usuario") as Usuario
            etNombre.setText(usuario.nombre_usuario)
            etEmail.setText(usuario.email)
            etNumero.setText(usuario.numero)
            etDireccion.setText(usuario.direccion)
            idUsuario = usuario.idUsuario

            val imgUri= ImageController.obtenerUri(this,idUsuario.toLong())
            imgFoto.setImageURI(imgUri)
        }

        val database = BaseDatos.getDatabase(this)

        btnGuardar.setOnClickListener {
            if (validarCamposVacios()) {
                val nombre = etNombre.text.toString()
                val email = etEmail.text.toString()
                val numero = etNumero.text.toString()
                val direccion = etDireccion.text.toString()

                val usuario = Usuario(nombre, email, numero, direccion, R.drawable.camera)

                if (idUsuario != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        usuario.idUsuario = idUsuario

                        database.usuarios().update(usuario)
                        imagenUri?.let {
                            ImageController.guardarImagen(
                                this@AgregarActivity,
                                idUsuario.toLong(),
                                it
                            )
                        }

                        finish()
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {

                        var id = database.usuarios().insertAll(usuario)[0]
                        imagenUri?.let {
                            val intent = Intent()
                            intent.data = it
                            setResult(Activity.RESULT_OK, intent)
                            ImageController.guardarImagen(this@AgregarActivity, id, it)
                        }
                        finish()
                    }
                }

                var comprobarUsuarios = emptyList<Usuario>()
                var database = BaseDatos.getDatabase(this)
                database.usuarios().getAll().observe(this, Observer {
                    comprobarUsuarios = it

                    if (comprobarUsuarios.isNotEmpty())
                        startActivity(Intent(this, MainActivity::class.java))
                })

            }

            }
            imgFoto.setOnClickListener {
                ImageController.seleccionarFoto(this, SELECCIONAR_ACTIVITY)

            }
        }

    private fun validarCamposVacios(): Boolean{
        var comprobar = true
        val nombre:String = etNombre.text.toString()
        val email:String = etEmail.text.toString()
        val numero:String = etNumero.text.toString()
        val direccion:String = etDireccion.text.toString()

        if (nombre.isEmpty()){
            etNombre.setError("Este campo no debe quedar en blanco")
            comprobar = false
        }
        if (email.isEmpty()){
            etEmail.setError("Este campo no debe quedar en blanco")
            comprobar = false
        }
        if (numero.isEmpty()){
            etNumero.setError("Este campo no debe quedar en blanco")
            comprobar = false
        }
        if (direccion.isEmpty()){
            etDireccion.setError("Este campo no debe quedar en blanco")
            comprobar = false
        }
        return comprobar
    }

    private fun comprobarErrores(){
        binding.etNombre.setOnFocusChangeListener { _, focused ->
            if(!focused)
                binding.nombreContainer.helperText = nombreValido()
        }
        binding.etEmail.setOnFocusChangeListener { _, focused ->
            if(!focused)
                binding.emailContainer.helperText = emailValido()
        }
        binding.etNumero.setOnFocusChangeListener { _, focused ->
            if(!focused)
                binding.numeroContainer.helperText = numeroValido()
        }
        binding.etDireccion.setOnFocusChangeListener { _, focused ->
            if(!focused)
                binding.direccionContainer.helperText = direccionValido()
        }
    }


    //VALIDAR NOMBRE
    private fun nombreValido() :String? {
        val nombre = binding.etNombre.text.toString()
        if (nombre.length <= 20)
            return "Nombre debe ser menor a 20 caracteres"
        return null
}
//    VALIDAR EMAIL
    private fun emailValido(): String? {
    val email = binding.etEmail.text.toString()

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return "Pon un correo valido"
    return null

    }
//    VALIDAR NUMERO
    private fun numeroValido(): String? {
        val numero = binding.etNumero.text.toString()
        if (numero.length != 10)
            return "El numero debe tener 10 digitos"
        return null
    }
//    VALIDAR DIRECCION
    private fun direccionValido(): String? {
        val direccion = binding.etDireccion.text.toString()
        if (direccion.length <= 30)
            return "La direccion debe ser menor que 30 caracteres"
        return null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == SELECCIONAR_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imagenUri = data!!.data

                imgFoto.setImageURI(imagenUri)
            }
        }

    }
}

