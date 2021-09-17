package com.gyenno.zero.medical.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Chon Den.
 *
 * @date 2021/09/17
 */
@Database(entities = [MessageRecorder::class], version = 1, exportSchema = false)
abstract class MessageDatabase : RoomDatabase() {

    abstract val messageDatabaseDao: MessageDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getInstance(context: Context): MessageDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MessageDatabase::class.java,
                        "message_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}