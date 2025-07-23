package com.artexplorer.artexplorer.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class MuseumApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    // Use the same data as the real museum app for authenticity
    private val apiUrl = "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"

    suspend fun getArtworks(): List<MuseumObject> {
        return try {
            // Add small delay to simulate real API
            delay(500)
            client.get(apiUrl).body()
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to hardcoded artworks if network fails
            getFallbackArtworks()
        }
    }

    private fun getFallbackArtworks(): List<MuseumObject> = listOf(
        MuseumObject(
            objectID = 436535,
            title = "Wheat Field with Cypresses",
            artistDisplayName = "Vincent van Gogh",
            medium = "Oil on canvas",
            dimensions = "28 7/8 × 36 3/4 in. (73.2 × 93.4 cm)",
            objectURL = "https://www.metmuseum.org/art/collection/search/436535",
            objectDate = "1889",
            primaryImage = "https://images.metmuseum.org/CRDImages/ep/original/DT1567.jpg",
            primaryImageSmall = "https://images.metmuseum.org/CRDImages/ep/web-large/DT1567.jpg",
            repository = "Metropolitan Museum of Art, New York, NY",
            department = "European Paintings",
            creditLine = "Purchase, The Annenberg Foundation Gift, 1993"
        ),
        MuseumObject(
            objectID = 436532,
            title = "Self-Portrait with a Straw Hat",
            artistDisplayName = "Vincent van Gogh",
            medium = "Oil on canvas",
            dimensions = "16 x 12 1/2 in. (40.6 x 31.8 cm)",
            objectURL = "https://www.metmuseum.org/art/collection/search/436532",
            objectDate = "1887",
            primaryImage = "https://images.metmuseum.org/CRDImages/ep/original/DT1502_cropped2.jpg",
            primaryImageSmall = "https://images.metmuseum.org/CRDImages/ep/web-large/DT1502_cropped2.jpg",
            repository = "Metropolitan Museum of Art, New York, NY",
            department = "European Paintings",
            creditLine = "Bequest of Miss Adelaide Milton de Groot (1876-1967), 1967"
        ),
        MuseumObject(
            objectID = 438817,
            title = "The Dance Class",
            artistDisplayName = "Edgar Degas",
            medium = "Oil on canvas",
            dimensions = "32 7/8 x 30 3/8 in. (83.5 x 77.2 cm)",
            objectURL = "https://www.metmuseum.org/art/collection/search/438817",
            objectDate = "1874",
            primaryImage = "https://images.metmuseum.org/CRDImages/ep/original/DP-20101-001.jpg",
            primaryImageSmall = "https://images.metmuseum.org/CRDImages/ep/web-large/DP-20101-001.jpg",
            repository = "Metropolitan Museum of Art, New York, NY",
            department = "European Paintings",
            creditLine = "Bequest of Mrs. Harry Payne Bingham, 1986"
        )
    )
}
