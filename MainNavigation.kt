package com.neonplayer.music.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neonplayer.music.model.Track
import com.neonplayer.music.viewmodel.MusicViewModel

@Composable
fun MainNavigation(viewModel: MusicViewModel) {
    val tracks by viewModel.tracks.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            Column {
                playerState.currentTrack?.let { currentTrack ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .background(Color(0xFF161228), RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentTrack.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = currentTrack.artist,
                                color = Color(0xFFA855F7),
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                        IconButton(onClick = { viewModel.togglePlayPause() }) {
                            Icon(
                                imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color(0xFF00F5D4)
                            )
                        }
                    }
                }

                NavigationBar(containerColor = Color(0xFF0A0914)) {
                    val items = listOf("Главная", "Медиатека", "Плейлисты", "Эквалайзер", "Настройки")
                    val icons = listOf(Icons.Default.Home, Icons.Default.LibraryMusic, Icons.Default.List, Icons.Default.Equalizer, Icons.Default.Settings)
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(icons[index], contentDescription = item) },
                            label = { Text(item, fontSize = 10.sp) },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFA855F7),
                                selectedTextColor = Color(0xFFA855F7),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF07070B))
                .padding(paddingValues)
        ) {
            if (tracks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "NEON PLAYER\nМедиатека готова к сканированию",
                        color = Color.LightGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    items(tracks) { track ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color(0xFF100E1D), RoundedCornerShape(12.dp))
                                .clickable { viewModel.playTrack(track) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(track.title, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text(track.artist, color = Color.Gray, fontSize = 12.sp)
                            }
                            Text(
                                text = "${track.duration / 60}:${(track.duration % 60).toString().padStart(2, '0')}",
                                color = Color(0xFFA855F7),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}