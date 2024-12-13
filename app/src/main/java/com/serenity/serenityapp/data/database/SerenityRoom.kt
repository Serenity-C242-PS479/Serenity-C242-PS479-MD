package com.serenity.serenityapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.serenity.serenityapp.data.model.database.Chat
import com.serenity.serenityapp.data.model.database.Sentiment
import com.serenity.serenityapp.data.model.database.User

@Database(entities = [User::class, Chat::class, Sentiment::class], version = 2, exportSchema = false)
abstract class SerenityRoom: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun sentimentDao(): SentimentDao

    companion object {
        @Volatile
        private var INSTANCE: SerenityRoom? = null

        @JvmStatic
        fun getDatabase(context: Context): SerenityRoom {
            if (INSTANCE == null) {
                synchronized(SerenityRoom::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SerenityRoom::class.java,
                        "serenity_database"
                    ).build()
                }
            }

            return INSTANCE as SerenityRoom
        }
    }
}