package com.example.hypermarket

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatScreen(itemId: String, currentUserId: String, onBack: () -> Unit) {
    val viewModel: ChatViewModel = viewModel()
    var messageText by remember { mutableStateOf("") }

    // Start listening to Firestore when the screen opens
    LaunchedEffect(itemId) {
        viewModel.listenForMessages(itemId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // 1. TOP BAR (Navigation)
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text("< Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Chatting about item", style = MaterialTheme.typography.titleMedium)
        }

        // 2. MESSAGE LIST (Middle)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(viewModel.messages) { msg ->
                val isMe = msg.senderId == currentUserId

                // Wrap in a Box to align left or right
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        modifier = Modifier.padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(text = msg.text, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }

        // 3. INPUT AREA (Bottom)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (messageText.isNotBlank()) {
                    viewModel.sendMessage(itemId, messageText, currentUserId)
                    messageText = "" // Clear field after sending
                }
            }) {
                Text("Send")
            }
        }
    }
}