package com.artexplorer.museum.data

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
            delay(800)
            val networkArtworks = client.get(apiUrl).body<List<MuseumObject>>()
            // Combine network data with extensive fallback collection
            networkArtworks + getFallbackArtworks()
        } catch (e: Exception) {
            e.printStackTrace()
            // Return extensive collection if network fails
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
            creditLine = "Purchase, The Annenberg Foundation Gift, 1993",
            description = "Van Gogh painted this masterpiece during his stay at the Saint-Paul-de-Mausole asylum. The swirling sky and flame-like cypresses represent his emotional turmoil while finding solace in nature's eternal rhythms."
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
            creditLine = "Bequest of Miss Adelaide Milton de Groot (1876-1967), 1967",
            description = "This intimate self-portrait captures van Gogh during his Paris period, experimenting with Impressionist techniques. The confident brushwork and vibrant colors show his artistic growth and self-assurance."
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
            creditLine = "Bequest of Mrs. Harry Payne Bingham, 1986",
            description = "Degas revolutionized ballet painting by capturing authentic behind-the-scenes moments. This work reveals the discipline and fatigue of dancers, humanizing performers often idealized in art."
        ),
        MuseumObject(
            objectID = 459123,
            title = "The Starry Night",
            artistDisplayName = "Vincent van Gogh",
            medium = "Oil on canvas",
            dimensions = "29 × 36 1/4 in. (73.7 × 92.1 cm)",
            objectURL = "https://www.moma.org/collection/works/79802",
            objectDate = "1889",
            primaryImage = "https://upload.wikimedia.org/wikipedia/commons/e/ea/Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg",
            primaryImageSmall = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg/500px-Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg",
            repository = "Museum of Modern Art, New York",
            department = "Post-Impressionist Paintings",
            creditLine = "Acquired through the Lillie P. Bliss Bequest",
            description = "Perhaps the most famous painting in the world, this swirling night sky represents van Gogh's unique vision of cosmic energy and spiritual transcendence, painted from memory and imagination."
        ),
        MuseumObject(
            objectID = 471253,
            title = "The Great Wave off Kanagawa",
            artistDisplayName = "Katsushika Hokusai",
            medium = "Polychrome woodblock print; ink and color on paper",
            dimensions = "10 1/16 × 14 15/16 in. (25.5 × 37.9 cm)",
            objectURL = "https://www.metmuseum.org/art/collection/search/45434",
            objectDate = "ca. 1830–32",
            primaryImage = "https://images.metmuseum.org/CRDImages/as/original/DP130155.jpg",
            primaryImageSmall = "https://images.metmuseum.org/CRDImages/as/web-large/DP130155.jpg",
            repository = "Metropolitan Museum of Art, New York, NY",
            department = "Asian Art",
            creditLine = "H. O. Havemeyer Collection, Bequest of Mrs. H. O. Havemeyer, 1929",
            description = "This iconic ukiyo-e print symbolizes the power of nature and human vulnerability. The wave's claw-like foam threatens Mount Fuji, representing the eternal struggle between human ambition and natural forces."
        ),
        MuseumObject(
            objectID = 482567,
            title = "Girl with a Pearl Earring",
            artistDisplayName = "Johannes Vermeer",
            medium = "Oil on canvas",
            dimensions = "17 1/2 × 15 3/8 in. (44.5 × 39 cm)",
            objectURL = "https://www.mauritshuis.nl/en/explore/the-collection/artworks/girl-with-a-pearl-earring-670/",
            objectDate = "c. 1665",
            primaryImage = "https://upload.wikimedia.org/wikipedia/commons/0/0f/1665_Girl_with_a_Pearl_Earring.jpg",
            primaryImageSmall = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/1665_Girl_with_a_Pearl_Earring.jpg/400px-1665_Girl_with_a_Pearl_Earring.jpg",
            repository = "Mauritshuis, The Hague",
            department = "Dutch Golden Age Paintings",
            creditLine = "Permanent Collection",
            description = "Vermeer's masterpiece captures an enigmatic moment of human connection. The girl's direct gaze and luminous pearl create an intimate dialogue across centuries, epitomizing Dutch mastery of light and emotion."
        ),
        MuseumObject(
            objectID = 493821,
            title = "The Persistence of Memory",
            artistDisplayName = "Salvador Dalí",
            medium = "Oil on canvas",
            dimensions = "9 1/2 × 13 in. (24.1 × 33 cm)",
            objectURL = "https://www.moma.org/collection/works/79018",
            objectDate = "1931",
            primaryImage = "https://upload.wikimedia.org/wikipedia/en/d/dd/The_Persistence_of_Memory.jpg",
            primaryImageSmall = "https://upload.wikimedia.org/wikipedia/en/thumb/d/dd/The_Persistence_of_Memory.jpg/400px-The_Persistence_of_Memory.jpg",
            repository = "Museum of Modern Art, New York",
            department = "Surrealism",
            creditLine = "Given anonymously",
            description = "Dalí's melting clocks challenge our perception of time's rigidity. This surrealist icon explores Einstein's relativity theory through dreamlike imagery, suggesting time's subjectivity in memory and consciousness."
        ),
        MuseumObject(
            objectID = 504392,
            title = "American Gothic",
            artistDisplayName = "Grant Wood",
            medium = "Oil on beaverboard",
            dimensions = "30 1/4 × 25 3/16 in. (76.8 × 63.9 cm)",
            objectURL = "https://www.artic.edu/artworks/6565/american-gothic",
            objectDate = "1930",
            primaryImage = "https://www.artic.edu/iiif/2/831a05de-d3f6-f4fa-a460-23008dd58dda/full/843,/0/default.jpg",
            primaryImageSmall = "https://www.artic.edu/iiif/2/831a05de-d3f6-f4fa-a460-23008dd58dda/full/400,/0/default.jpg",
            repository = "Art Institute of Chicago",
            department = "American Art",
            creditLine = "Friends of American Art Collection",
            description = "Wood's portrayal of rural American values became a cultural icon. The stern farmer and his daughter represent Midwestern virtues of hard work and moral integrity during the Great Depression era."
        ),
        MuseumObject(
            objectID = 515647,
            title = "The Birth of Venus",
            artistDisplayName = "Sandro Botticelli",
            medium = "Tempera on canvas",
            dimensions = "67.9 × 109.6 in. (172.5 × 278.9 cm)",
            objectURL = "https://www.uffizi.it/en/artworks/birth-of-venus-botticelli",
            objectDate = "c. 1484–1486",
            primaryImage = "https://upload.wikimedia.org/wikipedia/commons/0/0b/Sandro_Botticelli_-_La_nascita_di_Venere_-_Google_Art_Project_-_edited.jpg",
            primaryImageSmall = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Sandro_Botticelli_-_La_nascita_di_Venere_-_Google_Art_Project_-_edited.jpg/600px-Sandro_Botticelli_-_La_nascita_di_Venere_-_Google_Art_Project_-_edited.jpg",
            repository = "Uffizi Gallery, Florence",
            department = "Renaissance Paintings",
            creditLine = "Permanent Collection",
            description = "Botticelli's Renaissance masterpiece celebrates divine beauty and Neoplatonic love. Venus emerging from the sea represents spiritual awakening and the transformative power of beauty in human experience."
        ),
        MuseumObject(
            objectID = 526789,
            title = "Guernica",
            artistDisplayName = "Pablo Picasso",
            medium = "Oil on canvas",
            dimensions = "137.4 × 305.5 in. (349.3 × 776.6 cm)",
            objectURL = "https://www.museoreinasofia.es/en/collection/artwork/guernica",
            objectDate = "1937",
            primaryImage = "https://upload.wikimedia.org/wikipedia/en/7/74/PicassoGuernica.jpg",
            primaryImageSmall = "https://upload.wikimedia.org/wikipedia/en/thumb/7/74/PicassoGuernica.jpg/600px-PicassoGuernica.jpg",
            repository = "Museo Reina Sofía, Madrid",
            department = "Modern Art",
            creditLine = "Permanent Collection",
            description = "Picasso's powerful anti-war statement depicts the horror of the Spanish Civil War bombing. The fragmented forms and monochromatic palette convey universal anguish and humanity's capacity for both creation and destruction."
        ),
        MuseumObject(
            objectID = 537124,
            title = "The Scream",
            artistDisplayName = "Edvard Munch",
            medium = "Oil, tempera, and pastel on cardboard",
            dimensions = "36 × 28.9 in. (91 × 73.5 cm)",
            objectURL = "https://www.nasjonalmuseet.no/en/collection/object/NG.M.00939",
            objectDate = "1893",
            primaryImage = "https://upload.wikimedia.org/wikipedia/commons/c/c5/Edvard_Munch%2C_1893%2C_The_Scream%2C_oil%2C_tempera_and_pastel_on_cardboard%2C_91_x_73_cm%2C_National_Gallery_of_Norway.jpg",
            primaryImageSmall = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Edvard_Munch%2C_1893%2C_The_Scream%2C_oil%2C_tempera_and_pastel_on_cardboard%2C_91_x_73_cm%2C_National_Gallery_of_Norway.jpg/400px-Edvard_Munch%2C_1893%2C_The_Scream%2C_oil%2C_tempera_and_pastel_on_cardboard%2C_91_x_73_cm%2C_National_Gallery_of_Norway.jpg",
            repository = "National Museum of Norway, Oslo",
            department = "Expressionist Art",
            creditLine = "Permanent Collection",
            description = "Munch's existential masterpiece captures modern anxiety and isolation. The distorted figure against a blood-red sky expresses the artist's personal anguish and universal human fears of mortality and meaninglessness."
        ),
        MuseumObject(
            objectID = 548397,
            title = "Water Lilies",
            artistDisplayName = "Claude Monet",
            medium = "Oil on canvas",
            dimensions = "78.7 × 167.6 in. (200 × 425.8 cm)",
            objectURL = "https://www.moma.org/collection/works/80220",
            objectDate = "c. 1919",
            primaryImage = "https://upload.wikimedia.org/wikipedia/commons/a/aa/Claude_Monet_-_Water_Lilies_-_1919%2C_Metropolitan_Museum_of_Art.jpg",
            primaryImageSmall = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Claude_Monet_-_Water_Lilies_-_1919%2C_Metropolitan_Museum_of_Art.jpg/500px-Claude_Monet_-_Water_Lilies_-_1919%2C_Metropolitan_Museum_of_Art.jpg",
            repository = "Museum of Modern Art, New York",
            department = "Impressionist Paintings",
            creditLine = "Mrs. Simon Guggenheim Fund",
            description = "Monet's revolutionary series captures light's ephemeral qualities on water. These impressionistic studies of his Giverny garden represent a meditative exploration of nature's constant transformation and beauty."
        )
    )
}