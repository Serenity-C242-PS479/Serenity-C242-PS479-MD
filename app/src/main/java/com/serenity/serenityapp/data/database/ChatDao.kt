package com.serenity.serenityapp.data.database
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.serenity.serenityapp.data.model.database.Chat
import com.serenity.serenityapp.data.model.database.User

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chat: Chat)

    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: Int): Chat?

    @Query("SELECT * FROM chats WHERE user_id = :userId")
    suspend fun getChatsByUserId(userId: Int): List<Chat>

    @Query("SELECT * FROM chats WHERE user_id = :userId and predicted = 0")
    suspend fun getUnpredictedChatsByUserId(userId: Int): List<Chat>

    @Query("DELETE FROM chats")
    suspend fun deleteAllChat()
}