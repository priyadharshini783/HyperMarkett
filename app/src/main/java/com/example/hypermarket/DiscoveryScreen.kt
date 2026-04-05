package com.example.hypermarket

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun DiscoveryScreen(onOpenChat: (String) -> Unit, onBackToHome: () -> Unit) {
    // This line was failing because of missing imports
    val viewModel: DiscoveryViewModel = viewModel()

    // Coordinates near Chennai Institute of Technology
    val citChennai = LatLng(12.8231, 80.0412)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(citChennai, 13f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            viewModel.nearbyItems.forEach { item ->
                Marker(
                    state = MarkerState(position = LatLng(item.latitude, item.longitude)),
                    title = item.title,
                    snippet = "Price: ₹${item.price} - Tap to Chat",
                   onInfoWindowClick = {
                        onOpenChat(item.id) // This fixes the MainActivity error
                    }
                )
            }
        }
        IconButton(
            onClick = { onBackToHome() }, // Change this from currentScreen = "home"
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Go Home",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        // The "Search" button to trigger the 5km math
        Button(
            onClick = {
                viewModel.fetchNearbyItems(cameraPositionState.position.target)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp) // Adjusted so it doesn't overlap with FAB
        ) {
            Text("Search within 5km")
        }
    }
}