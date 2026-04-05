package com.example.hypermarket

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PostItemScreen(currentLocation: Location?, onPostSuccess: () -> Unit) {
    val viewModel: PostItemViewModel = viewModel()
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { currentLocation?.let { viewModel.uploadItem(title, price, desc, it, onPostSuccess) } },
            enabled = currentLocation != null && title.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (currentLocation != null) "Post Item" else "Waiting for GPS...")
        }
    }
}