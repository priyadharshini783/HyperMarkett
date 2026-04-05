package com.example.hypermarket

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val messages = mutableStateListOf<ChatMessage>()

    fun listenForMessages(itemId: String) {
        db.collection("items").document(itemId).collection("chats")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val newMessages = snapshot.toObjects(ChatMessage::class.java)
                    messages.clear()
                    messages.addAll(newMessages)
                }
            }
    }

    fun sendMessage(itemId: String, text: String, senderId: String) {
        if (text.isBlank()) return
        val message = ChatMessage(senderId = senderId, text = text)
        db.collection("items").document(itemId).collection("chats").add(message)
    }
}