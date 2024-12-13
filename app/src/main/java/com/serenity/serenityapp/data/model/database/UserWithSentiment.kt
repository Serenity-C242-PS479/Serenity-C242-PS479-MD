package com.serenity.serenityapp.data.model.database

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithSentiment(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val sentiments: List<Sentiment>
)