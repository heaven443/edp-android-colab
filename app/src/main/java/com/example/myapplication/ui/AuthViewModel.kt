package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.AppResult
import com.example.myapplication.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(loginError = "Please enter your email and password.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, loginError = null) }
        viewModelScope.launch {
            when (val result = repository.login(email, password)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, user = result.data) }
                }
                is AppResult.Failure -> {
                    val errorMsg = when (result) {
                        AppResult.Failure.NoInternet -> "No internet connection. Please try again."
                        AppResult.Failure.Timeout -> "Server timeout."
                        AppResult.Failure.WrongLogin -> "Wrong email or password."
                        AppResult.Failure.EmailTaken -> "An account with this email already exists."
                        is AppResult.Failure.Unknown -> result.msg ?: "Unknown error occurred."
                    }
                    _uiState.update { it.copy(isLoading = false, loginError = errorMsg) }
                }
            }
        }
    }

    fun register(fullName: String, email: String, password: String, birthdate: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank() || birthdate.isBlank()) {
            _uiState.update { it.copy(registerError = "Please fill in all four fields.") }
            return
        }
        if (!email.contains("@")) {
            _uiState.update { it.copy(registerError = "Please enter a valid email.") }
            return
        }
        if (password.length < 6) {
            _uiState.update { it.copy(registerError = "Password must be at least 6 characters.") }
            return
        }
        val birthdateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        if (!birthdateRegex.matches(birthdate)) {
            _uiState.update { it.copy(registerError = "Birthdate must look like 2004-05-17.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, registerError = null, registerSuccessMessage = null) }
        viewModelScope.launch {
            when (val result = repository.register(fullName, email, password, birthdate)) {
                is AppResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            registerSuccessMessage = "Account created for ${result.data.fullName}"
                        ) 
                    }
                }
                is AppResult.Failure -> {
                    val errorMsg = when (result) {
                        AppResult.Failure.NoInternet -> "No internet connection. Please try again."
                        AppResult.Failure.Timeout -> "Server timeout."
                        AppResult.Failure.WrongLogin -> "Wrong credentials."
                        AppResult.Failure.EmailTaken -> "An account with this email already exists."
                        is AppResult.Failure.Unknown -> result.msg ?: "Unknown error occurred."
                    }
                    _uiState.update { it.copy(isLoading = false, registerError = errorMsg) }
                }
            }
        }
    }

    fun logout() {
        _uiState.update { AuthUiState() }
    }

    fun clearErrors() {
        _uiState.update { it.copy(loginError = null, registerError = null, registerSuccessMessage = null) }
    }
}
