package com.lpu.android.sessionscreen.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsLogger(context: Context) {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logOtpGenerated(email: String) {
        val bundle = Bundle().apply {
            putString("email", email)
        }
        firebaseAnalytics.logEvent("otp_generated", bundle)
    }

    fun logOtpSuccess(email: String) {
        val bundle = Bundle().apply {
            putString("email", email)
        }
        firebaseAnalytics.logEvent("otp_validation_success", bundle)
    }

    fun logOtpFailure(email: String, reason: String) {
        val bundle = Bundle().apply {
            putString("email", email)
            putString("reason", reason)
        }
        firebaseAnalytics.logEvent("otp_validation_failure", bundle)
    }

    fun logLogout(email: String) {
        val bundle = Bundle().apply {
            putString("email", email)
        }
        firebaseAnalytics.logEvent("logout", bundle)
    }
    fun logOtpTimerStarted(email: String) {
        firebaseAnalytics.logEvent("otp_timer_started", Bundle().apply {
            putString("email", email)
        })
    }

    fun logOtpExpired(email: String) {
        firebaseAnalytics.logEvent("otp_expired", Bundle().apply {
            putString("email", email)
        })
    }

    fun logOtpResent(email: String, remainingSeconds: Long) {
        firebaseAnalytics.logEvent("otp_resent", Bundle().apply {
            putString("email", email)
            putLong("remaining_seconds", remainingSeconds)
        })
    }

}
