package com.lpu.android.sessionscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun OtpScreen(
    email: String,
    remainingTime: Long,
    attemptsLeft: Int,
    onVerify: (String) -> Unit,
    onResend: () -> Unit
) {
    var otp by remember { mutableStateOf("") }
    val seconds = remainingTime / 1000

    CommonScreen(title = "Verify OTP") {

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // Email display
                Text(
                    text = "OTP sent to",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(12.dp))

                // Status row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "⏱ ${seconds}s",
                        color = if (seconds > 0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                    )
                    Text("Attempts: $attemptsLeft")
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("Enter 6-digit OTP") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { onVerify(otp) },
                    enabled = otp.length == 6 && attemptsLeft > 0,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Verify")
                }

                Spacer(Modifier.height(8.dp))

                TextButton(
                    onClick = onResend,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Resend OTP")
                }
            }
        }
    }
}
