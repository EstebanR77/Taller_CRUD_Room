package com.ejemplo.crudroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Producto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile private var INSTANCIA: AppDatabase? = null
        fun obtener(context: Context): AppDatabase = INSTANCIA ?: synchronized(this) {
            INSTANCIA ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "productos.db"
            ).build().also { INSTANCIA = it }
        }
    }
}
