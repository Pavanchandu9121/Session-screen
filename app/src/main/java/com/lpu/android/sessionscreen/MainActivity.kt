package com.lpu.android.sessionscreen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lpu.android.sessionscreen.analytics.AnalyticsLogger
import com.lpu.android.sessionscreen.viewModel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val analyticsLogger = AnalyticsLogger(this)

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(analyticsLogger)
            )

            LaunchedEffect(Unit) {
                authViewModel.uiEvent.collect { event ->
                    when (event) {
                        is AuthUiEvent.ShowToast -> {
                            Toast.makeText(
                                this@MainActivity,
                                event.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            val state = authViewModel.state.collectAsState().value

            when (state) {
                is AuthState.EmailInput ->
                    LoginScreen { authViewModel.sendOtp(it) }

                is AuthState.OtpInput ->
                    OtpScreen(
                        email = state.email,
                        remainingTime = state.remainingTime,
                        attemptsLeft = state.attemptsLeft,
                        onVerify = { authViewModel.verifyOtp(state.email, it) },
                        onResend = { authViewModel.resendOtp(state.email) }
                    )

                is AuthState.LoggedIn ->
                    SessionScreen(
                        email = state.email,
                        duration = state.duration,
                        sessionStart = state.sessionStart
                    ) { authViewModel.logout(state.email) }

                is AuthState.Error ->
                    LoginScreen { authViewModel.sendOtp(it) }
            }
        }
    }
}
