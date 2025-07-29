package com.example.dirtystream

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
        try {
            Log.d("DirtyStream", "Attempting to load native library 'libdummy_library.so'")
            System.loadLibrary("dummy_library")
        } catch (e: UnsatisfiedLinkError) {
            Log.e("DirtyStream", "Could not load dummy_library, this is expected before the attack.")
        }
        // logging
        Log.d("DirtyStream", "Initialising default settings file.");
        val sharedPref = getSharedPreferences("com.example.dirtystream_preferences", MODE_PRIVATE)
        val editor = sharedPref.edit()
        if (!sharedPref.contains("init_key")) {
            editor.putString("init_key", "initial_value")
            editor.apply()
        }

        // STEP 3: Handle files sent from other apps - THE ATTACK VECTOR!
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

    // In vulnerableapp's MainActivity.kt

    private fun handleIncomingFile(uri: Uri) {
        // The function now starts with a simple try-catch block
        // with no UI feedback.
        try {
            val fileName = getFileName(uri)
            Log.d("DirtyStream", "Processing file silently: '$fileName'") // Log for debugging

            if (fileName == null) {
                Log.e("DirtyStream", "Silent fail: Filename is null.")
                return // Exit quietly
            }

            // This is the core file write logic. It remains the same,
            // but all user-facing messages are removed.
            val tempFile = File(filesDir, fileName)
            val canonicalPath = tempFile.canonicalPath
            val outputFile = File(canonicalPath)

            if (canonicalPath.startsWith("/storage/emulated/0")) {
                val externalDir = getExternalFilesDir(null)
                if (externalDir != null) {
                    val finalExternalFile = File(externalDir.absolutePath, outputFile.name)
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        FileOutputStream(finalExternalFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
            } else {
                val parentDir = outputFile.parentFile
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs()
                }
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(outputFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }

            // The operation is now completely invisible to the user.
            Log.d("DirtyStream", "Silent file operation complete.")

        } catch (e: Exception) {
            // Even if an error occurs, we fail silently.
            // An attacker doesn't want to alert the user that something went wrong.
            Log.e("DirtyStream", "Silent fail: Error handling file", e)
        }
    }

    // STEP 7: Extract filename from URI: attackers can manipulate here
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
                text = "Easily receive and organise shared files",
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
                        text = "• Other apps can share files directly with FileShare Pro\n• Browse and select files manually\n• All content is safely organised in your app folder\n• Perfect for receiving photos, documents, and more!",
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