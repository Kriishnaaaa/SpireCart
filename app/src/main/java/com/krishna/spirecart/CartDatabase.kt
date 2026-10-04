
package com.krishna.spirecart

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CartItem::class],
    version = 1,
    exportSchema = false
)
abstract class CartDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CartDatabase::class.java,
                    "spirecart_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}