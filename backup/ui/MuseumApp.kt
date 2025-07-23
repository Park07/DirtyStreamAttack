// File: attackapp/src/main/java/com/artexplorer/museum/ui/MuseumApp.kt
package com.artexplorer.artexplorer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.artexplorer.museum.data.MuseumObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuseumApp(
    onShareArtwork: (MuseumObject) -> Unit,
    viewModel: MuseumViewModel = viewModel()
) {
    val artworks by viewModel.artworks.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var selectedArtwork by remember { mutableStateOf<MuseumObject?>(null) }

    // Load artworks when app starts
    LaunchedEffect(Unit) {
        viewModel.loadArtworks()
    }

    Column {
        // Beautiful app bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Palette,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Art Explorer",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Main content
        if (selectedArtwork != null) {
            ArtworkDetailScreen(
                artwork = selectedArtwork!!,
                onBack = { selectedArtwork = null },
                onShare = { onShareArtwork(selectedArtwork!!) }
            )
        } else {
            ArtworkGridScreen(
                artworks = artworks,
                loading = loading,
                onArtworkClick = { selectedArtwork = it },
                onShare = onShareArtwork
            )
        }
    }
}

@Composable
fun ArtworkGridScreen(
    artworks: List<MuseumObject>,
    loading: Boolean,
    onArtworkClick: (MuseumObject) -> Unit,
    onShare: (MuseumObject) -> Unit
) {
    when {
        loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Loading beautiful artworks...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        artworks.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No artworks available",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(artworks) { artwork ->
                    ArtworkCard(
                        artwork = artwork,
                        onClick = { onArtworkClick(artwork) },
                        onShare = { onShare(artwork) }
                    )
                }
            }
        }
    }
}

@Composable
fun ArtworkCard(
    artwork: MuseumObject,
    onClick: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Artwork image
            AsyncImage(
                model = artwork.primaryImageSmall,
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            // Artwork info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = artwork.artistDisplayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = artwork.objectDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    IconButton(
                        onClick = { onShare() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share artwork",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
