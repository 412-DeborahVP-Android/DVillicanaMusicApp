package com.example.dvillicaamusicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dvillicaamusicapp.ImageLoaderProvider
import com.example.dvillicaamusicapp.data.Album
import com.example.dvillicaamusicapp.data.RetrofitInstance
import com.example.dvillicaamusicapp.ui.components.MiniPlayer

@Composable
fun HomeScreen(
    onAlbumClick: (String) -> Unit
) {
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            albums = RetrofitInstance.api.getAlbums()
            isLoading = false
        } catch (e: Exception) {
            error = e.message
            isLoading = false
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF7B5EA7))
        }
        return
    }

    if (error != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $error", color = Color.Red)
        }
        return
    }

    Box(Modifier.fillMaxSize().background(Color(0xFFF5F0FA))) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { HomeHeader() }
            item { AlbumsSection(albums, onAlbumClick) }
            item { RecentlyPlayedHeader() }
            items(albums) { album ->
                RecentlyPlayedItem(album, onAlbumClick)
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
        MiniPlayer(
            album = albums.firstOrNull(),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF9C6ADE), Color(0xFFB98DE0), Color(0xFFCDA4F0))
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.height(12.dp))
                        Text("Good Morning!", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                        Text(
                            "Deborah Villicaña",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun AlbumsSection(albums: List<Album>, onAlbumClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Albums", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C1B1F))
        Text("See more", color = Color(0xFF7B5EA7), fontSize = 14.sp)
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(albums) { album ->
            AlbumCard(album, onAlbumClick)
        }
    }
}

@Composable
private fun AlbumCard(album: Album, onAlbumClick: (String) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(230.dp)
            .clickable { onAlbumClick(album.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D1B4E))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(album.image)
                    .crossfade(true)
                    .build(),
                imageLoader = ImageLoaderProvider.get(context),
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2D1B4E))
                    .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            album.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                        Text(
                            album.artist,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF7B5EA7), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentlyPlayedHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Recently Played", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C1B1F))
        Text("See more", color = Color(0xFF7B5EA7), fontSize = 14.sp)
    }
}

@Composable
private fun RecentlyPlayedItem(album: Album, onAlbumClick: (String) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clickable { onAlbumClick(album.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(album.image)
                    .crossfade(true)
                    .build(),
                imageLoader = ImageLoaderProvider.get(context),
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(album.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, maxLines = 1)
                Text(
                    "${album.artist} • Popular Song",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }
            Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
        }
    }
}
