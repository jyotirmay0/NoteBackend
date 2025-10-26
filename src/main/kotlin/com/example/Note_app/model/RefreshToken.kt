package com.example.Note_app.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import java.time.Instant


@Document(collection="refresh_token")
data class RefreshToken (
    val userId: ObjectId = ObjectId(),
    @Indexed(expireAfter = "0s")
    val expiredAt: Instant,
    val hashedToken: String,
    val createdAt: Instant = Instant.now()
)