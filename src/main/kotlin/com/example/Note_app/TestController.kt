package com.example.Note_app
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController 
import org.springframework.beans.factory.annotation.Value  

@RestController
class TestController(@Value("\${SPRING_DATA_MONGODB_URI:NOT_FOUND}") val dbUrl: String) {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello from Note App Backend 👋"
    }

    @GetMapping("/status")
    fun status(): Map<String, String> {
        return mapOf(
            "status" to "Running ✅",
            "message" to "Spring Boot Kotlin backend is working perfectly!"
        )
    }
 
    @GetMapping("/env")
    fun showEnv() = mapOf("SPRING_DATA_MONGODB_URI" to dbUrl)
}