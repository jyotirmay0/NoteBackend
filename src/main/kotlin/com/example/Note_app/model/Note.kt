package com.example.Note_app.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "notes")
data class Note(
    @Id 
    val id: ObjectId? = null,  // Changed from ObjectId.get()
    
    val title: String,
    val content: String,
    
    @CreatedDate
    val createdAt: Instant = Instant.now(),  // Changed to Instant
    
    val ownerId: ObjectId
)