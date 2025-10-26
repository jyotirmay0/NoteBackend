package com.example.Note_app.Repo

import com.example.Note_app.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}