package com.example.spora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.spora.bd.BaseDatos
import com.example.spora.usuario.Usuario
import com.example.spora.usuario.UsuarioAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lista_usuarios.*

class MainActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fabAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarActivity::class.java))
        }

        var listaUsuarios = emptyList<Usuario>()
        val database = BaseDatos.getDatabase(this)

        database.usuarios().getAll().observe(this, Observer {
            listaUsuarios = it

        val adapter = UsuarioAdapter(this, listaUsuarios)
        listView.adapter = adapter
        })


        listView.setOnItemClickListener{parent, view,position, id ->
            val intent = Intent(this, VerActivity::class.java)
            intent.putExtra("id",listaUsuarios[position].idUsuario)
            startActivity(intent)
        }


    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cerrar_sesion, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.salir)
            AlertDialog.Builder(this)
                .setMessage("¿Salir de la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Si") { dialog, whichButton ->
                    finishAffinity()
                }
                .setNegativeButton("Cancelar") { dialog, whichButton ->

                }
                .show()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("¿Salir de la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Si") { dialog, whichButton ->
                finishAffinity()
            }
            .setNegativeButton("Cancelar") { dialog, whichButton ->

            }
            .show()
    }
}