package com.example.myapplication.ui

import com.example.myapplication.domain.model.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val loginError: String? = null,
    val registerError: String? = null,
    val registerSuccessMessage: String? = null
)
