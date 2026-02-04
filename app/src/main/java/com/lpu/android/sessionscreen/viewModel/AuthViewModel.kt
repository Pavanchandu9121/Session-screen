package com.lpu.android.sessionscreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lpu.android.sessionscreen.analytics.AnalyticsLogger
import com.lpu.android.sessionscreen.data.OtpManager
import com.lpu.android.sessionscreen.data.OtpResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


class AuthViewModel(
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent: SharedFlow<AuthUiEvent> = _uiEvent
    private val otpManager = OtpManager()
    private var otpTimerJob: Job? = null

    private val _state = MutableStateFlow<AuthState>(AuthState.EmailInput)
    val state: StateFlow<AuthState> = _state

    private var timerJob: Job? = null

    fun sendOtp(email: String) {
        otpManager.generateOtp(email)
        analyticsLogger.logOtpGenerated(email)
        analyticsLogger.logOtpTimerStarted(email)

        viewModelScope.launch {
            _uiEvent.emit(AuthUiEvent.ShowToast("OTP sent successfully"))
        }
        startOtpTimer(email)
    }


    fun verifyOtp(email: String, otp: String) {
        when (val result = otpManager.validateOtp(email, otp)) {

            is OtpResult.Success -> {
                analyticsLogger.logOtpSuccess(email)
                viewModelScope.launch {
                    _uiEvent.emit(AuthUiEvent.ShowToast("Login successful"))
                }
                startSession(email)
            }

            is OtpResult.Wrong -> {
                analyticsLogger.logOtpFailure(email, "wrong_otp")
                viewModelScope.launch {
                    _uiEvent.emit(
                        AuthUiEvent.ShowToast(
                            "Incorrect OTP. Attempts left: ${result.attemptsLeft}"
                        )
                    )
                }
            }

            is OtpResult.Expired -> {
                viewModelScope.launch {
                    _uiEvent.emit(
                        AuthUiEvent.ShowToast("OTP expired. Please resend")
                    )
                }
            }

            is OtpResult.AttemptsExceeded -> {
                viewModelScope.launch {
                    _uiEvent.emit(
                        AuthUiEvent.ShowToast("Maximum attempts exceeded")
                    )
                }
            }

            else -> Unit
        }
    }


    private fun startSession(email: String) {
        val startTime = System.currentTimeMillis()

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val duration = System.currentTimeMillis() - startTime
                _state.value = AuthState.LoggedIn(email, startTime, duration)
                delay(1000)
            }
        }
    }
    private fun startOtpTimer(email: String) {
        otpTimerJob?.cancel()

        otpTimerJob = viewModelScope.launch {
            while (true) {
                val remainingTime = otpManager.getRemainingTime(email)
                val attemptsLeft = otpManager.getRemainingAttempts(email)

                if (remainingTime <= 0) {
                    analyticsLogger.logOtpExpired(email)

                    _state.value = AuthState.OtpInput(
                        email = email,
                        remainingTime = 0L,
                        attemptsLeft = otpManager.getRemainingAttempts(email)
                    )
                    break
                }



                _state.value = AuthState.OtpInput(
                    email = email,
                    remainingTime = remainingTime,
                    attemptsLeft = attemptsLeft
                )

                delay(1000)
            }
        }
    }
    private suspend fun showToast(message: String) {
        _uiEvent.emit(AuthUiEvent.ShowToast(message))
    }

    fun resendOtp(email: String) {
        val remainingSeconds =
            otpManager.getRemainingTime(email) / 1000

        analyticsLogger.logOtpResent(email, remainingSeconds)

        otpManager.generateOtp(email)

        viewModelScope.launch {
            _uiEvent.emit(AuthUiEvent.ShowToast("New OTP generated"))
        }

        startOtpTimer(email)
    }


    fun logout(email: String) {
        timerJob?.cancel()
        analyticsLogger.logLogout(email)

        viewModelScope.launch {
            _uiEvent.emit(AuthUiEvent.ShowToast("Logged out successfully"))
        }

        _state.value = AuthState.EmailInput
    }


}
