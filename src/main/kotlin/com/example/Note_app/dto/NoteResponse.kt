package com.example.Note_app.dto

data class NoteResponse(
    val id: String,
    val title: String,
    val content: String,
    val ownerId: String,
    val createdAt: String,
    val message: String
)