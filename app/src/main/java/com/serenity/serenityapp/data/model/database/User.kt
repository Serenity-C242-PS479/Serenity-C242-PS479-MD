package com.serenity.serenityapp.data.model.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String,
): Parcelable {
    fun avatar(): String {
        return "https://ui-avatars.com/api/?background=00BFA6&size=128&name=${name}"
    }
}
