package com.example.dvillicaamusicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dvillicaamusicapp.data.Album
import com.example.dvillicaamusicapp.data.RetrofitInstance
import com.example.dvillicaamusicapp.ui.components.MiniPlayer

@Composable
fun DetailScreen(
    albumId: String,
    onBack: () -> Unit
) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(albumId) {
        try {
            album = RetrofitInstance.api.getAlbumById(albumId)
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

    if (error != null || album == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: ${error ?: "Album not found"}", color = Color.Red)
        }
        return
    }

    val currentAlbum = album!!
    val tracks = (1..10).map { "${currentAlbum.title} • Track $it" }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0FA))
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { DetailHeader(currentAlbum, onBack) }
            item { AboutSection(currentAlbum) }
            item { ArtistChip(currentAlbum.artist) }
            items(tracks) { track ->
                TrackItem(track, currentAlbum)
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
        MiniPlayer(
            album = currentAlbum,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun DetailHeader(album: Album, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        AsyncImage(
            model = album.image,
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x66000000),
                            Color(0xAA7B5EA7),
                            Color(0xDDF5F0FA)
                        )
                    )
                )
        )
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        IconButton(
            onClick = { },
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Favorite, contentDescription = "Favorite", tint = Color.White)
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(album.title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(album.artist, color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF7B5EA7), CircleShape)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF5C3D8F), CircleShape)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Shuffle", tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun AboutSection(album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("About this album", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text(album.description, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ArtistChip(artist: String) {
    Surface(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFEDE7F6),
        tonalElevation = 0.dp
    ) {
        Text(
            text = "Artist: $artist",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 13.sp,
            color = Color(0xFF5C3D8F)
        )
    }
}

@Composable
private fun TrackItem(track: String, album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
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
                model = album.image,
                contentDescription = track,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(track, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1)
                Text(album.artist, color = Color.Gray, fontSize = 12.sp)
            }
            Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
        }
    }
}
