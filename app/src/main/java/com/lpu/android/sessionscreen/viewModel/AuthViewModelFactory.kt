package com.lpu.android.sessionscreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lpu.android.sessionscreen.analytics.AnalyticsLogger

class AuthViewModelFactory(
    private val analyticsLogger: AnalyticsLogger
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(analyticsLogger) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}