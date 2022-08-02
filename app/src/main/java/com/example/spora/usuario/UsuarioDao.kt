package com.example.spora.usuario

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsuarioDao {
    @Query("Select * FROM usuarios ORDER BY idusuario DESC")
    fun getAll(): LiveData<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE email = :email")
    fun getEmail(email:String): LiveData<List<Usuario>>

    @Query("SELECT * FROM usuarios ORDER BY idUsuario DESC LIMIT 1")
    fun getUser(): LiveData<Usuario>

    @Query("SELECT * FROM usuarios WHERE idUsuario = :id")
    fun get(id: Int): LiveData<Usuario>

    @Insert
    fun insertAll(vararg usuarios: Usuario): List<Long>

    @Update
    fun update(usuario:Usuario)

    @Delete
    fun delete(usuario:Usuario)
}