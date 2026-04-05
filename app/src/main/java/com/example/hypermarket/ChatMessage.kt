package com.example.hypermarket

/**
 * Data model for a single chat message.
 * Using default values (= "") is mandatory for Firebase to work.
 */
data class ChatMessage(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)