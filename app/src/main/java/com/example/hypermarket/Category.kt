package com.example.hypermarket

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

data class Category(val name: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

val categories = listOf(
    // --- CAMPUS & EDUCATION ---
    Category("Textbooks", Icons.Default.LibraryBooks),
    Category("Notes", Icons.Default.Description),
    Category("Lab Kits", Icons.Default.PrecisionManufacturing),
    Category("Calculators", Icons.Default.Calculate),
    Category("Drafting", Icons.Default.Architecture),
    Category("Project Parts", Icons.Default.Memory), // Great for IoT/Hardware!

    // --- ELECTRONICS & GADGETS ---
    Category("Smartphones", Icons.Default.Smartphone),
    Category("Laptops", Icons.Default.Laptop),
    Category("Tablets", Icons.Default.TabletAndroid),
    Category("Headphones", Icons.Default.Headphones),
    Category("Gaming", Icons.Default.Gamepad),
    Category("Cameras", Icons.Default.PhotoCamera),
    Category("Accessories", Icons.Default.Usb),

    // --- FASHION & LIFESTYLE ---
    Category("Men's Wear", Icons.Default.Male),
    Category("Women's Wear", Icons.Default.Female),
    Category("Footwear", Icons.Default.Hiking),
    Category("Watches", Icons.Default.Watch),
    Category("Bags", Icons.Default.Work),
    Category("Jewelry", Icons.Default.Diamond),

    // --- HOME & APPLIANCES ---
    Category("Furniture", Icons.Default.Chair),
    Category("Appliances", Icons.Default.Kitchen),
    Category("Decoration", Icons.Default.Home),
    Category("Kitchenware", Icons.Default.Flatware),
    Category("Bedding", Icons.Default.Bed),

    // --- TRANSPORT & OUTDOORS ---
    Category("Bikes", Icons.Default.TwoWheeler),
    Category("Cars", Icons.Default.DirectionsCar),
    Category("Cycles", Icons.Default.DirectionsBike),
    Category("Sports Gear", Icons.Default.SportsBasketball),
    Category("Gym", Icons.Default.FitnessCenter),

    // --- MISCELLANEOUS ---
    Category("Music", Icons.Default.MusicNote),
    Category("Art", Icons.Default.Brush),
    Category("Hobbies", Icons.Default.AutoAwesome),
    Category("Other", Icons.Default.MoreHoriz)
)