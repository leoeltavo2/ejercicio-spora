package com.example.spora.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spora.usuario.Usuario
import com.example.spora.usuario.UsuarioDao

@Database(entities = [Usuario::class], version = 1)
abstract class BaseDatos:RoomDatabase() {

    abstract  fun usuarios(): UsuarioDao

    companion object{
        @Volatile
        private var INSTANCE: BaseDatos? = null

        fun getDatabase(context: Context): BaseDatos{
            val tempInstance = INSTANCE

            if(tempInstance != null)
                return tempInstance
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatos::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}