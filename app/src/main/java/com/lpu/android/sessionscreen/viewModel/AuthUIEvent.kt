package com.lpu.android.sessionscreen.viewModel

sealed class AuthUiEvent {
    data class ShowToast(val message: String) : AuthUiEvent()
}
