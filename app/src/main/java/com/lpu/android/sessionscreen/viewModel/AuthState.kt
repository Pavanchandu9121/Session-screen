package com.lpu.android.sessionscreen.viewModel

sealed class AuthState {
    object EmailInput : AuthState()
    data class OtpInput(
        val email: String,
        val remainingTime: Long,
        val attemptsLeft: Int
    ) : AuthState()
    data class LoggedIn(
        val email: String,
        val sessionStart: Long,
        val duration: Long
    ) : AuthState()

    data class Error(val message: String) : AuthState()

}
