package com.lpu.android.sessionscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SessionScreen(
    email: String,
    duration: Long,
    sessionStart: Long,
    onLogout: () -> Unit
) {
    val minutes = (duration / 1000) / 60
    val seconds = (duration / 1000) % 60

    val startTime = remember(sessionStart) {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            .format(Date(sessionStart))
    }

    CommonScreen(title = "Session") {

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Logged in as",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(12.dp))

                Text("Session started at: $startTime")
                Text(
                    text = "Duration: %02d:%02d".format(minutes, seconds),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }
    }
}
