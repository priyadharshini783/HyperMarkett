package com.example.hypermarket

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.hypermarket.ui.theme.HyperMarketTheme
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private var currentLocation by mutableStateOf<Location?>(null)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            getUserLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        checkAndRequestPermissions()

        setContent {
            HyperMarketTheme {
                var isReady by remember { mutableStateOf(false) }

                // 1. FIX: Added missing closing parenthesis
                val favoriteItems = remember { mutableStateListOf<String>() }

                // Navigation States
                var currentScreen by remember { mutableStateOf("home") }
                var selectedItemId by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    delay(800)
                    isReady = true
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!isReady) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        // --- NAVIGATION SWITCHBOARD ---
                        when (currentScreen) {
                            "home" -> {
                                HomeScreen(
                                    onNavigateToMap = { currentScreen = "discovery" },
                                    onNavigateToSell = { currentScreen = "post" },
                                    onNavigateToSaved = { currentScreen = "saved" }, // 2. New Link
                                    favoriteItems = favoriteItems // 3. Pass the list
                                )
                            }

                            "post" -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    PostItemScreen(
                                        currentLocation = currentLocation,
                                        onPostSuccess = { currentScreen = "home" }
                                    )
                                    TextButton(
                                        onClick = { currentScreen = "home" },
                                        modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                                    ) {
                                        Text("< Back")
                                    }
                                }
                            }

                            "discovery" -> {
                                DiscoveryScreen(
                                    onOpenChat = { itemId ->
                                        selectedItemId = itemId
                                        currentScreen = "chat"
                                    },
                                    onBackToHome = {
                                        currentScreen = "home"
                                    }
                                )
                            }

                            "chat" -> {
                                ChatScreen(
                                    itemId = selectedItemId,
                                    currentUserId = "user_123",
                                    onBack = { currentScreen = "discovery" }
                                )
                            }

                            "saved" -> { // 4. ADDED SAVED SCREEN CASE
                                SavedScreen(
                                    favoriteItems = favoriteItems,
                                    onBack = { currentScreen = "home" }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getUserLocation()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                currentLocation = location
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}