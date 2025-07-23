package com.artexplorer.artexplorer.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.artexplorer.museum.data.MuseumObject


class MuseumRepository {
    private val api = MuseumApi()

    private val _artworks = MutableStateFlow<List<MuseumObject>>(emptyList())
    val artworks: StateFlow<List<MuseumObject>> = _artworks.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    suspend fun loadArtworks() {
        _loading.value = true
        try {
            val artworkList = api.getArtworks()
            _artworks.value = artworkList
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _loading.value = false
        }
    }

    fun getArtworkById(id: Int): MuseumObject? {
        return _artworks.value.find { it.objectID == id }
    }
}
