package com.example.myapplication.data.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MessageDto(
    val id: String? = null,
    val sender: String? = null,
    val text: String? = null,
    val createdAt: JsonElement? = null
)

@Serializable
data class NewMessageDto( // GIVEN (read it, do not change it)
    val sender: String, // no id here — the server makes it
    val text: String,
    val createdAt: Long
)
