package com.example.Note_app.security

import com.example.Note_app.Repo.RefreshTokenRepository
import com.example.Note_app.Repo.UserRepository
import com.example.Note_app.model.RefreshToken
import com.example.Note_app.model.User
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashencoder: hashencoder,
    private val refreshTokenRepository: RefreshTokenRepository) {

    data class tokenpair(
        val access_token: String,
        val refresh_token: String,
    )

    fun register(email: String, password: String): User {
        if (userRepository.existsByEmail(email)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User with this email already exists"
            )
        }
        return userRepository.save(
            User(
                email=email,
                hassedpassword = hashencoder.encode(password)
            )
        )
    }

    fun login(email: String, password: String): tokenpair{
        val user=userRepository.findByEmail(email)
            ?:throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,"Credention invalid")

        if(!hashencoder.matches(password, user.hassedpassword)){
             throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,"Passwords do not match")
        }
        val newAccesstoken=jwtService.generateAccessToken(userId = user.id.toHexString())
        val newRefreshtoken=jwtService.generateRefrehToken(userId = user.id.toHexString())

        storeRfreshtoken(user.id,newRefreshtoken)
        return tokenpair(newAccesstoken,newRefreshtoken)
    }

    fun storeRfreshtoken(userId: ObjectId, refreshToken: String) {
        val hashedtoken=hashToken(refreshToken)
        val expire= Instant.now().plusMillis(jwtService.refreshtokenValidity)
        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                hashedToken = hashedtoken,
                expiredAt = expire

            )
        )
    }
@Transactional
    fun refreshToken(refreshToken: String): tokenpair{
        if (!jwtService.validateRefreshToken(refreshToken)){
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,"Refresh token is invalid")
        }
        val userId=jwtService.getUserIdFromToken(refreshToken)
        val user=userRepository.findById(ObjectId(userId)).orElseThrow{
            throw ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,"invalid refresh token")
        }
        val hashed=hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(user.id,hashed)
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,"invalid refresh token")
        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id,hashed)

        val newAccesstoken=jwtService.generateAccessToken(userId = user.id.toHexString())
        val newrefreshtoken=jwtService.generateRefrehToken(userId = user.id.toHexString())
        storeRfreshtoken(user.id,newrefreshtoken)

        return tokenpair(newAccesstoken,
            newrefreshtoken)


    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)

    }





}
