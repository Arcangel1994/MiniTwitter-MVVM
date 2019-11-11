package com.example.minitwitter.valid

import android.util.Patterns

class Valid {

    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}