package com.example.Note_app.controller

import com.example.Note_app.Repo.NoteRepo
import com.example.Note_app.model.Note
import com.example.Note_app.dto.NoteResponse
import com.example.Note_app.security.SecurityConfig
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/notes")  // Added /api prefix
class NoteController(private val noteRepo: NoteRepo) {
    
    data class NoteRequest(
        val title: String,
        val content: String
    )

    @GetMapping()
    fun getNotesByOwnerId(): List<Note> {
        val ownerId= SecurityContextHolder.getContext().authentication.principal as String

        return noteRepo.findByOwnerId(ObjectId(ownerId))
    }

    @PostMapping
    fun createNote(@RequestBody body: NoteRequest): NoteResponse {
        val ownerId= SecurityContextHolder.getContext().authentication.principal as String

        val note = Note(
            id=null,
            title = body.title,
            content = body.content,
            ownerId = ObjectId(ownerId), // Generate ownerId automatically
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

        val note= noteRepo.findById(ObjectId(id)).orElseThrow {
            throw IllegalArgumentException(" Note not found")
        }

        val ownerId= SecurityContextHolder.getContext().authentication.principal as String

        if(note.ownerId.toHexString() == ownerId) {
            noteRepo.deleteById(ObjectId(id))

        }
        return "Note deleted successfully ✅"
    }
    

}