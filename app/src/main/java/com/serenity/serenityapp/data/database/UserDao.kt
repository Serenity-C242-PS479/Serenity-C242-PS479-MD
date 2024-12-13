package com.serenity.serenityapp.data.database
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.serenity.serenityapp.data.model.database.User
import com.serenity.serenityapp.data.model.database.UserWithSentiment

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>

    @Transaction
    @Query("SELECT * FROM users order by id desc")
    suspend fun getUsersWithSentiments(): List<UserWithSentiment>

    @Query("SELECT * FROM users WHERE name = :userName")
    suspend fun getUserByName(userName: String): User?
}