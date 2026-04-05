package com.example.hypermarket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.snapshots.SnapshotStateList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToSell: () -> Unit,
    onNavigateToSaved: () -> Unit, // Navigation instruction
    favoriteItems: SnapshotStateList<String> // Shared favorite list
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("HyperMarket", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                },
                actions = {
                    // Watchlist Button
                    IconButton(onClick = onNavigateToSaved) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Watchlist")
                    }
                    // Notifications
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {

            // --- 1. SEARCH BAR ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search items", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            // --- 2. PROMO BANNER ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5)))
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text("Clear the Hostel Clutter!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Sell your old books & kits today.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    }
                }
            }

            // --- 3. CATEGORIES ---
            Text(
                text = "Explore Categories",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(categories) { category ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                                .clickable { /* Filter Action */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(category.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                        }
                        Text(text = category.name, fontSize = 10.sp, modifier = Modifier.padding(top = 6.dp), color = Color.DarkGray)
                    }
                }
            }

            // --- 4. ACTION BUTTONS ---
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onNavigateToMap,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Map")
                }
                OutlinedButton(
                    onClick = onNavigateToSell,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Sell")
                }
            }

            // --- 5. NEARBY ITEMS GRID ---
            Text(
                text = "Recently Added Nearby",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            val recentItems = listOf("Biege Sofa", "iPhone 13", "Graphics Kit", "Study Lamp")

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(recentItems) { item ->
                    ItemCard(
                        name = item,
                        itemId = item,
                        isFavorite = favoriteItems.contains(item),
                        onToggleFavorite = { id ->
                            if (favoriteItems.contains(id)) favoriteItems.remove(id)
                            else favoriteItems.add(id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemCard(
    name: String,
    itemId: String,
    isFavorite: Boolean,
    onToggleFavorite: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box {
            Column {
                // Image placeholder
                Box(modifier = Modifier.fillMaxWidth().height(110.dp).background(Color.LightGray))

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                    Text(text = "₹ 1,200", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(10.dp), tint = Color.Gray)
                        Text("1.2 km away", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }

            // Heart Icon positioned in the top right
            IconButton(
                onClick = { onToggleFavorite(itemId) },
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
    }
}