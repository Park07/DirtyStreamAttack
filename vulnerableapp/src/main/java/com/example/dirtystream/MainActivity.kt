package com.example.dirtystream

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

// ADD THESE MISSING COMPOSE IMPORTS:
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.dirtystream.ui.theme.DirtyStreamTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    // STEP 1: Add state variables to track what's happening
    private var statusMessage by mutableStateOf("📱 Ready to receive shared content")

    // STEP 2: Set up file picker (when user clicks button manually)
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Log.d("DirtyStream", "User selected file: $uri")
            handleIncomingFile(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 3: Handle files sent from other apps (THE ATTACK VECTOR!)
        handleIncomingIntent(intent)

        setContent {
            DirtyStreamTheme {
                UnsuspectingScreen(
                    statusMessage = statusMessage,
                    onBrowseClick = {
                        // When user clicks button, open file picker
                        filePickerLauncher.launch("*/*")
                    }
                )
            }
        }
    }

    // STEP 4: Handle new intents (when app is already running and receives a file)
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIncomingIntent(it) }
    }

    // STEP 5: Check if another app is sending us a file
    private fun handleIncomingIntent(intent: Intent) {
        Log.d("DirtyStream", "Received intent: ${intent.action}")

        if (intent.action == Intent.ACTION_SEND) {
            val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
            uri?.let {
                statusMessage = "📥 Received shared content!"
                Log.d("DirtyStream", "Another app shared with us: $uri")
                handleIncomingFile(it)
            }
        }
    }

    // STEP 6: Process the file - THIS IS WHERE THE VULNERABILITY IS!
    private fun handleIncomingFile(uri: Uri) {
        try {
            // Get the filename from the URI
            val fileName = getFileName(uri)
            Log.d("DirtyStream", "Processing file: '$fileName'")

            // 🚨 VULNERABILITY: Using filename directly without validation!
            val outputFile = File(filesDir, fileName ?: "shared_content.txt")

            Log.d("DirtyStream", "Saving to: ${outputFile.absolutePath}")

            // Show attack detection in UI!
            if (fileName?.contains("../") == true) {
                statusMessage = "🚨 PATH TRAVERSAL ATTACK DETECTED!\nMalicious filename: $fileName"
                Toast.makeText(this, "⚠️ DIRTYSTREAM ATTACK!\nPath: ${outputFile.absolutePath}", Toast.LENGTH_LONG).show()
            } else {
                statusMessage = "✅ Content saved successfully!"
            }

            // Copy the file content
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            statusMessage = "✅ Content saved: ${fileName ?: "shared_content.txt"}"
            Toast.makeText(this, "Content saved to: ${outputFile.absolutePath}", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            statusMessage = "❌ Error: ${e.message}"
            Log.e("DirtyStream", "Error handling file", e)
        }
    }

    // STEP 7: Extract filename from URI (this is where attackers can manipulate!)
    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null

        // Query the content resolver to get file info
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = cursor.getString(nameIndex)
                    Log.d("DirtyStream", "Extracted filename: '$fileName'")
                }
            }
        }
        return fileName
    }
}

@Composable
fun UnsuspectingScreen(
    statusMessage: String,
    onBrowseClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {

            // App title - looks innocent
            Text(
                text = "📂 FileShare Pro",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Easily receive and organize shared files",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Status display
            Card(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = statusMessage,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            // Main browse button - looks innocent
            Button(onClick = onBrowseClick) {
                Text("📁 Browse Files")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions that look helpful
            Card(
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💡 How it works:",
                        style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "• Other apps can share files directly with FileShare Pro\n• Browse and select files manually\n• All content is safely organized in your app folder\n• Perfect for receiving photos, documents, and more!",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Start,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DirtyStreamTheme {
        UnsuspectingScreen(
            statusMessage = "📱 Ready to receive shared content",
            onBrowseClick = {}
        )
    }
}