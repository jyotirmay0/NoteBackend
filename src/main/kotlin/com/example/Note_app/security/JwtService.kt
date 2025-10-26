package com.example.Note_app.security

import com.fasterxml.jackson.databind.ser.Serializers
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Base64
import java.util.Date
import kotlin.reflect.typeOf


@Service
class JwtService(
   @Value("\${Jwt.secretKey}") private val JwtsecretKey:String
){

    private val secretKey= Keys.hmacShaKeyFor(Base64.getDecoder().decode(JwtsecretKey))
    private val accsesstokenValididty=15L*60L*1000L
    val refreshtokenValidity=30L*24*60*60L*1000L

    fun genaratetoken(
        userId: String,
        type: String,
        expiry: Long
    ): String {
        val now= Date()
        val exp=Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("type",type)
            .issuedAt(now)
            .expiration(exp)
            .signWith(secretKey,Jwts.SIG.HS256)
        .compact()
    }

     fun generateAccessToken(userId: String): String{
       return genaratetoken(userId,"access", accsesstokenValididty)
    }
     fun generateRefrehToken(userId: String): String{
        return genaratetoken(userId,"refresh", accsesstokenValididty)
    }
    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "refresh"
    }

    fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
            "Invalid token."
        )
        return claims.subject
    }

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if(token.startsWith("Bearer ")) {
            token.removePrefix("Bearer ")
        } else token
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch(e: Exception) {
            null
        }
    }

}