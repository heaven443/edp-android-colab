package com.example.myapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun ProfileScreen(
    uiState: AuthUiState,
    onLogoutClick: () -> Unit
) {
    val user = uiState.user ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "You successfully logged in!",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Full Name: ${user.fullName}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Birthdate: ${user.birthdate}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        val age = ageFrom(user.birthdate)
        if (age != null) {
            Text(text = "Age: $age years old", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(text = "User ID: ${user.id}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onLogoutClick, modifier = Modifier.fillMaxWidth()) {
            Text("Log out")
        }
    }
}

fun ageFrom(birthdate: String): Int? {
    val parts = birthdate.split("-")
    if (parts.size != 3) return null
    val birthYear = parts[0].toIntOrNull() ?: return null
    val birthMonth = parts[1].toIntOrNull() ?: return null
    val birthDay = parts[2].toIntOrNull() ?: return null

    val today = Calendar.getInstance()
    val todayYear = today.get(Calendar.YEAR)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayDay = today.get(Calendar.DAY_OF_MONTH)

    var age = todayYear - birthYear
    if (todayMonth < birthMonth || (todayMonth == birthMonth && todayDay < birthDay)) {
        age--
    }
    return age
}
