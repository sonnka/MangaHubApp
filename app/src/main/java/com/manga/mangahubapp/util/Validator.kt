package com.manga.mangahubapp.util

import android.util.Log
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Validator {

    fun validatePhoneNumber(phoneNumber: String): String? {
        try {
            val phone = phoneNumber.trim().replace("\\s".toRegex(), "")
            val pattern = "^[+]?[0-9]{12}$"
            Log.d("Phone", phone)
            val isMatch = Regex(pattern).matches(phone)
            return if (isMatch) {
                phone
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
        return null
    }

    fun validateEmail(email: String): String? {
        email.trim()
        val pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        val isMatch = Regex(pattern).matches(email)
        return if (isMatch) {
            email
        } else {
            null
        }
    }

    fun validateLogin(login: String): String? {
        login.trim()
        val pattern = "^[a-zA-Z0-9_-]{4,32}\$"
        val isMatch = Regex(pattern).matches(login)
        return if (isMatch) {
            login
        } else {
            null
        }
    }

    fun validatePassword(email: String): String? {
        email.trim()
        val pattern = "^[a-zA-Z0-9!@#\$%^&*-_]{4,32}\$"
        val isMatch = Regex(pattern).matches(email)
        return if (isMatch) {
            email
        } else {
            null
        }
    }

    fun validateDate(date: String): String? {
        date.trim()
        val inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val localDate = LocalDate.parse(date, inputFormat)

        val currentTime = ZonedDateTime.now(ZoneId.of("UTC"))

        val combinedDateTime =
            ZonedDateTime.of(localDate, currentTime.toLocalTime(), ZoneId.of("UTC"))

        val outputFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        return combinedDateTime.format(outputFormat)
    }
}