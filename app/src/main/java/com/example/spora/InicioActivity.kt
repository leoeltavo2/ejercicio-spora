package com.example.spora

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.spora.bd.BaseDatos
import com.example.spora.image.ImageController
import com.example.spora.usuario.Usuario
import kotlinx.android.synthetic.main.activity_agregar.*
import kotlinx.android.synthetic.main.activity_inicio.*

class InicioActivity : AppCompatActivity() {
    private lateinit var database: BaseDatos
    private lateinit var email: Usuario
    private lateinit var imagen: Usuario


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        var listaUsuarios = emptyArray<Usuario>()
        database = BaseDatos.getDatabase(this)
            database.usuarios().getUser().observe(this, Observer{
                if(it != null) {
                    email = it
                    etEmailUsuario.setText("${email.email}")
                    val idUsuario = intent.getIntExtra("id", 0)
                    val imgUri = ImageController.obtenerUri(this, idUsuario.toLong())
                    imgInicio.setImageURI(imgUri)
                }
            })



        btnIrMain.setOnClickListener {
            val emailUsuario = etEmailUsuario.text.toString()

            val database = BaseDatos.getDatabase(this)
            database.usuarios().getEmail(emailUsuario).observe(this, Observer {
                listaUsuarios = it.toTypedArray()
                if (listaUsuarios.isEmpty())
                    startActivity(Intent(this, AgregarActivity::class.java))
                else
                    startActivity(Intent(this, MainActivity::class.java))
            })
        }
    }
}