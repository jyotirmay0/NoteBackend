package com.example.Note_app.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class hashencoder {

    private val bcrypt = BCryptPasswordEncoder()

    fun encode(password: String): String = bcrypt.encode(password)

    fun matches(hass: String, password: String): Boolean = bcrypt.matches(hass, password)


}