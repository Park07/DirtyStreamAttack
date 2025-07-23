
// File: attackapp/src/main/java/com/artexplorer/museum/ui/MuseumViewModel.kt
package com.artexplorer.museum.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artexplorer.museum.data.MuseumObject
import com.artexplorer.museum.data.MuseumRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MuseumViewModel : ViewModel() {
    private val repository = MuseumRepository()

    val artworks: StateFlow<List<MuseumObject>> = repository.artworks
    val loading: StateFlow<Boolean> = repository.loading

    fun loadArtworks() {
        viewModelScope.launch {
            repository.loadArtworks()
        }
    }

    fun getArtworkById(id: Int): MuseumObject? {
        return repository.getArtworkById(id)
    }
}