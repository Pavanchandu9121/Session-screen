package com.lpu.android.sessionscreen.data

import android.util.Log
import java.security.SecureRandom

data class OtpData(
    val otp: String,
    val createdAt: Long,
    var attemptsLeft: Int
)

class OtpManager {

    private val otpStore = mutableMapOf<String, OtpData>()

    private val OTP_EXPIRY_MS = 60_000L
    private val MAX_ATTEMPTS = 3

    fun generateOtp(email: String): String {

        val secureRandom = SecureRandom()
        val otp = (secureRandom.nextInt(900000) + 100000).toString()
        otpStore[email] = OtpData(
            otp = otp,
            createdAt = System.currentTimeMillis(),
            attemptsLeft = MAX_ATTEMPTS
        )
        Log.d("OTP_DEBUG", "OTP for $email is $otp")

        return otp
    }

    fun validateOtp(email: String, inputOtp: String): OtpResult {
        val data = otpStore[email] ?: return OtpResult.Invalid

        if (isExpired(data)) {
            otpStore.remove(email)
            return OtpResult.Expired
        }

        if (data.attemptsLeft <= 0) {
            return OtpResult.AttemptsExceeded
        }

        return if (data.otp == inputOtp) {
            otpStore.remove(email)
            OtpResult.Success
        } else {
            data.attemptsLeft--
            OtpResult.Wrong(data.attemptsLeft)
        }
    }

    private fun isExpired(data: OtpData): Boolean {
        return System.currentTimeMillis() - data.createdAt > OTP_EXPIRY_MS
    }
    fun getRemainingTime(email: String): Long {
        val data = otpStore[email] ?: return 0L
        val elapsed = System.currentTimeMillis() - data.createdAt
        val remaining = OTP_EXPIRY_MS - elapsed
        return if (remaining > 0) remaining else 0L
    }

    fun getRemainingAttempts(email: String): Int {
        return otpStore[email]?.attemptsLeft ?: 0
    }

}

sealed class OtpResult {
    object Success : OtpResult()
    object Expired : OtpResult()
    object Invalid : OtpResult()
    object AttemptsExceeded : OtpResult()
    data class Wrong(val attemptsLeft: Int) : OtpResult()
}
