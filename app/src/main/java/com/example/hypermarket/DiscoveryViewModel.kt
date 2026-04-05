package com.example.hypermarket

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Tasks

class DiscoveryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // This list holds the items found within the 5km radius
    val nearbyItems = mutableStateListOf<MarketItem>()

    fun fetchNearbyItems(userLocation: LatLng) {
        val center = GeoLocation(userLocation.latitude, userLocation.longitude)
        val radiusInMeters = 5000.0 // 5km

        // 1. Calculate the Geohash bounds for the 5km circle
        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInMeters)
        val tasks = mutableListOf<com.google.android.gms.tasks.Task<com.google.firebase.firestore.QuerySnapshot>>()

        for (b in bounds) {
            val q = db.collection("items")
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }

        // 2. Collect all results and filter out "false positives" (items in the square but outside the circle)
        Tasks.whenAllComplete(tasks).addOnCompleteListener {
            val allFound = mutableListOf<MarketItem>()
            for (task in tasks) {
                val snap = task.result as? com.google.firebase.firestore.QuerySnapshot
                snap?.documents?.forEach { doc ->
                    val item = doc.toObject(MarketItem::class.java)
                    if (item != null) {
                        val docLocation = GeoLocation(item.latitude, item.longitude)
                        val distanceInMeters = GeoFireUtils.getDistanceBetween(docLocation, center)

                        // Only add if it's actually within 5km
                        if (distanceInMeters <= radiusInMeters) {
                            allFound.add(item.copy(id = doc.id))
                        }
                    }
                }
            }
            // Update the UI list
            nearbyItems.clear()
            nearbyItems.addAll(allFound)
        }
    }
}