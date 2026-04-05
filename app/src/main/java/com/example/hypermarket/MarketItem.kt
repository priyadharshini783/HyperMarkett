package com.example.hypermarket

data class MarketItem(
    val id: String = "",
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val geohash: String = "",
    val sellerId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)