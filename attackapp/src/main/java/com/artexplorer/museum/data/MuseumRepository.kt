package com.artexplorer.museum.data

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MuseumRepository {
    private val api = MuseumApi()

    private val _artworks = MutableStateFlow<List<MuseumObject>>(emptyList())
    val artworks: StateFlow<List<MuseumObject>> = _artworks.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    suspend fun loadArtworks() {
        Log.d("MuseumRepository", "Starting to load artworks...")
        _loading.value = true
        try {
            val artworkList = api.getArtworks()
            Log.d("MuseumRepository", "Successfully fetched ${artworkList.size} artworks.")
            _artworks.value = artworkList
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _loading.value = false
            Log.d("MuseumRepository", "Finished loading artworks.")
        }
    }

    fun getArtworkById(id: Int): MuseumObject? {
        return _artworks.value.find { it.objectID == id }
    }
}
