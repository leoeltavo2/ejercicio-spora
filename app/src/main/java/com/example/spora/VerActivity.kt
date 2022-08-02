package com.example.spora

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.spora.bd.BaseDatos
import com.example.spora.image.ImageController
import com.example.spora.usuario.Usuario
import kotlinx.android.synthetic.main.activity_agregar.*
import kotlinx.android.synthetic.main.activity_ver.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VerActivity : AppCompatActivity() {
    private lateinit var database: BaseDatos
    private lateinit var usuario: Usuario
    private lateinit var usuarioLiveData:LiveData<Usuario>
    private val EDITAR_ACTIVITY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver)

        database = BaseDatos.getDatabase(this)
        val idUsuario = intent.getIntExtra("id",0)
        val imgUri= ImageController.obtenerUri(this,idUsuario.toLong())
        imgVerPerfil.setImageURI(imgUri)

        usuarioLiveData = database.usuarios().get(idUsuario)
        usuarioLiveData.observe(this, Observer{
            usuario = it
        tvVerNombre.text = usuario.nombre_usuario
        tvVerEmail.text = usuario.email
        tvVerNumero.text = usuario.numero
        tvVerDireccion.text = usuario.direccion
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.acciones, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.Editar ->{
                val intent = Intent(this, AgregarActivity::class.java)
                intent.putExtra("usuario", usuario)
                startActivityForResult(intent, EDITAR_ACTIVITY)
            }
            R.id.Eliminar ->{
                usuarioLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.usuarios().delete(usuario)
                    ImageController.EliminarImagen(this@VerActivity, usuario.idUsuario.toLong())
                    finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when{
            requestCode == EDITAR_ACTIVITY && resultCode == Activity.RESULT_OK ->{

                imgFoto.setImageURI(data!!.data)
            }
        }

    }
}