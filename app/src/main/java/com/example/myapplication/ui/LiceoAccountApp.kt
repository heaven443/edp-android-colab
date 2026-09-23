package com.example.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LiceoAccountApp(
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentScreen by remember { mutableStateOf("login") }

    if (uiState.user != null) {
        currentScreen = "profile"
    } else if (currentScreen == "profile") {
        currentScreen = "login"
    }

    when (currentScreen) {
        "login" -> {
            LoginScreen(
                uiState = uiState,
                onLogin = { email, password -> viewModel.login(email, password) },
                onCreateAccountClick = {
                    viewModel.clearErrors()
                    currentScreen = "register"
                }
            )
        }
        "register" -> {
            RegisterScreen(
                uiState = uiState,
                onRegister = { fullName, email, password, birthdate ->
                    viewModel.register(fullName, email, password, birthdate)
                },
                onBackToLoginClick = {
                    viewModel.clearErrors()
                    currentScreen = "login"
                }
            )
        }
        "profile" -> {
            ProfileScreen(
                uiState = uiState,
                onLogoutClick = {
                    viewModel.logout()
                    currentScreen = "login"
                }
            )
        }
    }
}
