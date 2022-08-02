package com.example.spora.usuario

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.spora.R
import com.example.spora.image.ImageController
import kotlinx.android.synthetic.main.lista_usuarios.view.*

class UsuarioAdapter(private val contexto:Context, private val listaUsuarios: List<Usuario>): ArrayAdapter<Usuario>(contexto,0,listaUsuarios) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(contexto).inflate(R.layout.lista_usuarios, parent,false)
        val usuario = listaUsuarios[position]

        layout.tvUsuario.text = usuario.nombre_usuario
        layout.tvEmail.text = usuario.email
        layout.tvNumero.text = usuario.numero
        layout.tvDireccion.text = usuario.direccion
        val imgUri= ImageController.obtenerUri(contexto,usuario.idUsuario.toLong())
        layout.imgPerfil.setImageURI(imgUri)

        return layout
    }
}