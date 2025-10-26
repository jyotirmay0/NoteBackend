package com.example.Note_app.controller

import com.example.Note_app.Repo.NoteRepo
import com.example.Note_app.model.Note
import com.example.Note_app.dto.NoteResponse
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/notes")  // Added /api prefix
class NoteController(private val noteRepo: NoteRepo) {
    
    data class NoteRequest(
        val title: String,
        val content: String
    )

    @GetMapping("/{ownerId}")
    fun getNotesByOwnerId(@PathVariable ownerId: String): List<Note> {
        return noteRepo.findByOwnerId(ObjectId(ownerId))
    }

    @PostMapping
    fun createNote(@RequestBody body: NoteRequest): NoteResponse {
        val note = Note(
            id=null,
            title = body.title,
            content = body.content,
            ownerId = ObjectId(), // Generate ownerId automatically
            createdAt = Instant.now()
        )
        val savedNote = noteRepo.save(note)

        return NoteResponse(
            id = savedNote.id?.toHexString() ?: "",
            title = savedNote.title,
            content = savedNote.content,
            ownerId = savedNote.ownerId.toHexString(),
            createdAt = savedNote.createdAt.toString(),
            message = "Note saved successfully ✅"
        )
    }

    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable id: String): String {
        noteRepo.deleteById(ObjectId(id))
        return "Note deleted successfully ✅"
    }
    
    @GetMapping
    fun getAllNotes(): List<Note> {
        return noteRepo.findAll()
    }
}