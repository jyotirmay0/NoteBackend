package com.example.Note_app

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import com.example.Note_app.Repo.NoteRepo

@SpringBootTest
class NoteAppApplicationTests {

	@MockBean
	private lateinit var noteRepo: NoteRepo

	@Test
	fun contextLoads() {
		// Context loads successfully
	}
}