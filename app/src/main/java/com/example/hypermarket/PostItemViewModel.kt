package com.example.hypermarket

import android.location.Location
import androidx.lifecycle.ViewModel
import com.firebase.geofire.util.GeoUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation

class PostItemViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun uploadItem(title: String, price: String, desc: String, loc: Location, onSuccess: () -> Unit) {
        val hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(loc.latitude, loc.longitude))

        val item = MarketItem(
            title = title,
            price = price,
            description = desc,
            latitude = loc.latitude,
            longitude = loc.longitude,
            geohash = hash,
            sellerId = auth.currentUser?.uid ?: "anonymous"
        )

        db.collection("items").add(item).addOnSuccessListener { onSuccess() }
    }
}