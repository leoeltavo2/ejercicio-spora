package com.example.spora.usuario

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "usuarios")
class Usuario(var nombre_usuario:String, var email:String, var numero:String, var direccion:String, var foto: Int,
            @PrimaryKey(autoGenerate = true)
            var idUsuario:Int = 0
): Serializable {
}