package com.artexplorer.artexplorer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.artexplorer.museum.data.MuseumObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworkDetailScreen(
    artwork: MuseumObject,
    onBack: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar with back button and share
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                FilledTonalButton(
                    onClick = onShare,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Artwork")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Large artwork image
            AsyncImage(
                model = artwork.primaryImage.ifEmpty { artwork.primaryImageSmall },
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            // Artwork title and artist
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "by ${artwork.artistDisplayName}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Artwork details in cards
            ArtworkInfoCard("Date", artwork.objectDate)
            ArtworkInfoCard("Medium", artwork.medium)
            ArtworkInfoCard("Dimensions", artwork.dimensions)
            ArtworkInfoCard("Department", artwork.department)
            ArtworkInfoCard("Repository", artwork.repository)

            if (artwork.creditLine.isNotEmpty()) {
                ArtworkInfoCard("Credit", artwork.creditLine)
            }

            // Share prompt - THIS ENCOURAGES THE ATTACK!
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Love this artwork? Share it with friends!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )

                    Button(
                        onClick = onShare,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share This Masterpiece")
                    }
                }
            }

            // Bottom spacing
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ArtworkInfoCard(label: String, value: String) {
    if (value.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}