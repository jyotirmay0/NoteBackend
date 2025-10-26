package com.example.Note_app.controller

import com.example.Note_app.model.RefreshToken
import com.example.Note_app.security.AuthService
import jakarta.validation.constraints.Email
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
) {

    data class AuthRequest(
        val email: String,
        val password: String
    )

    data class RefreshRequest(
       val refreshToken: String
    )


    @PostMapping("/register")
    fun register(
        @RequestBody body: AuthRequest
    ){
        authService.register(body.email,body.password)
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthService.tokenpair{
        return authService.refreshToken(body.refreshToken)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: AuthRequest
    ): AuthService.tokenpair {
        return authService.login(body.email,body.password)
    }


}