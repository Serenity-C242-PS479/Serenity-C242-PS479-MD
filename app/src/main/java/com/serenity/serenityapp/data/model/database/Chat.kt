package com.serenity.serenityapp.data.model.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "chats")
@Parcelize
data class Chat(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "predicted")
    val predicted: Boolean = false,
): Parcelable
