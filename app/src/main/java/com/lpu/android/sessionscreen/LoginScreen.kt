package com.lpu.android.sessionscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun LoginScreen(onSendOtp: (String) -> Unit) {
    var email by remember { mutableStateOf("") }

    CommonScreen(title = "Login") {

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Enter your email",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { onSendOtp(email) },
                    enabled = email.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send OTP")
                }
            }
        }
    }
}
