package com.example.Note_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NoteAppApplication

fun main(args: Array<String>) {
	runApplication<NoteAppApplication>(*args)
}
