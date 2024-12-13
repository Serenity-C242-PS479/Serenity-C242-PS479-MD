package com.serenity.serenityapp.data.database
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.serenity.serenityapp.data.model.database.Chat
import com.serenity.serenityapp.data.model.database.Sentiment
import com.serenity.serenityapp.data.model.database.User

@Dao
interface SentimentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sentiment: Sentiment)

    @Query("SELECT * FROM sentiments WHERE id = :sentimentId")
    suspend fun getSentimentById(sentimentId: Int): Sentiment?

    @Query("SELECT * FROM sentiments WHERE user_id = :userId")
    suspend fun getSentimentsByUserId(userId: Int): List<Sentiment>
}