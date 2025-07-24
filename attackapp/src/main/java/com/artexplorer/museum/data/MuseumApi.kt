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
            description = "Van Gogh painted this work while recovering in a mental health asylum, using bold, swirling brushstrokes that became his signature style. Notice how the cypress trees seem to dance like flames reaching toward the turbulent sky - this technique is called 'impasto,' where paint is applied thickly to create texture. The curved lines and vibrant yellows and blues show how van Gogh used color and movement to express his emotions rather than just copy what he saw. This painting demonstrates Post-Impressionism, an art movement that focused on the artist's personal feelings and interpretations. Van Gogh's energetic brushwork influenced countless modern artists and helped change how we think about painting."
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
            description = "This self-portrait shows van Gogh experimenting with Impressionist techniques he learned while living in Paris. Look closely at how he uses small dots and dashes of pure color side by side - this technique is called 'pointillism' and creates vibrant effects when viewed from a distance. The straw hat and bright colors reflect the influence of Japanese art, which was very popular in Paris at the time. Self-portraits were important for artists to practice their skills and express their identity, especially for van Gogh who couldn't afford to hire models. This painting captures a more optimistic period in van Gogh's life, before his mental health struggles intensified."
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
            description = "Degas revolutionised how artists painted ballet by showing the hard work behind the glamorous performances. Notice how the composition is cropped like a photograph - Degas was influenced by the new technology of photography and Japanese prints, which often cut off figures at unusual angles. The painting captures a real moment of instruction, with tired dancers stretching and listening to their teacher. Degas used pastels and oil paint together to create soft, realistic skin tones and flowing fabric textures. This work represents Impressionism's focus on everyday modern life rather than grand historical or mythological subjects."
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
            description = "Perhaps the world's most famous painting, this masterpiece shows van Gogh's unique ability to make the invisible visible - notice how the wind and energy in the night sky become swirling, rhythmic patterns. Van Gogh painted this from memory and imagination while in an asylum, combining his view from his window with a Dutch church steeple from his childhood. The thick application of paint (impasto) creates a three-dimensional texture that makes the stars and moon seem to glow. The complementary colors of blue and yellow create vibrant contrast and emotional intensity. This painting represents the peak of Post-Impressionism, where artists expressed inner feelings rather than just recording what they observed."
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
            description = "This iconic image is actually a woodblock print, meaning Hokusai carved the design into wooden blocks, applied ink, and pressed paper onto them to create multiple copies. The technique, called ukiyo-e ('floating world pictures'), made art affordable for ordinary Japanese people, not just the wealthy. Notice how the wave's foam creates claw-like shapes that seem to grab at Mount Fuji in the distance - this shows nature's power over humans. The composition uses dramatic perspective and bold outlines that influenced Western artists like van Gogh and Monet. This print demonstrates Japanese mastery of line, pattern, and the ability to capture a single dramatic moment in time."
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
            description = "Vermeer was a master of light, and this painting shows his incredible skill at capturing how light falls on different surfaces - notice how the pearl seems to actually glow. This isn't a traditional portrait but a 'tronie,' a Dutch term for a character study that focuses on expression and exotic costume rather than depicting a specific person. The mysterious girl's direct gaze creates an intimate connection with viewers that has captivated people for centuries. Vermeer used expensive ultramarine blue (made from ground lapis lazuli) for the turban, showing this was an important work. The simple composition and soft, natural lighting demonstrate the Dutch Golden Age's focus on everyday beauty and technical perfection."
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
            description = "Dalí's melting clocks represent his famous 'paranoiac-critical method,' where he painted his dreams and subconscious thoughts with photographic precision. This technique, called Surrealism, emerged after World War I when artists wanted to explore the mind's hidden depths rather than just the visible world. The landscape shows Dalí's hometown in Spain, but the impossible melting timepieces suggest that time in memory and dreams doesn't follow normal rules. Notice how Dalí painted with incredible detail and smoothness, making the impossible seem real - this contrast between realistic technique and dreamlike content is key to Surrealism. The painting reflects Einstein's theory of relativity, which showed that time isn't as fixed as people once believed."
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
            description = "Wood painted this during the Great Depression to celebrate traditional American values of hard work and moral strength. The models were actually Wood's sister and his dentist, posed in front of a real Gothic Revival house in Iowa that inspired the painting. Notice the precise, smooth painting technique that makes every detail crystal clear - this style is called 'American Regionalism,' which rejected European modern art in favor of realistic American subjects. The vertical lines of the pitchfork echo the board-and-batten siding and Gothic window, creating visual harmony. This painting became an icon of American identity, though people still debate whether Wood intended to honor or gently mock rural American life."
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
            description = "This Renaissance masterpiece shows the Roman goddess Venus arriving on shore after being born from sea foam, representing divine love and beauty. Botticelli used tempera paint, which dries quickly and creates the clear, bright colors you see here - this was the preferred medium before oil painting became popular. Notice how Venus's pose and proportions follow classical Greek and Roman sculpture, showing how Renaissance artists revived ancient ideals of beauty. The painting demonstrates 'linear perspective,' where the shoreline and figures create depth, and 'contrapposto,' where Venus's weight shifts create a natural, graceful pose. This work represents the Renaissance belief that physical beauty could inspire spiritual enlightenment and divine love."
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
            description = "Picasso created this powerful anti-war painting in response to the bombing of Guernica during the Spanish Civil War, using his Cubist style to show the horror of modern warfare. Notice how figures are broken into geometric fragments and shown from multiple angles simultaneously - this technique helps express the chaos and violence of the attack. The monochromatic palette of black, white, and gray creates a newspaper-like quality, emphasizing that this is about real events, not mythology. Symbols like the bull (Spain), horse (suffering people), and light bulb (bomb's destruction) work together to create a universal statement against war. At nearly 12 feet tall and 25 feet wide, the massive size makes viewers feel surrounded by the tragedy, demonstrating art's power to bear witness to historical events."
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
            description = "Munch painted this after experiencing a moment when he felt 'a scream passing through nature' during a walk at sunset, making this one of the first artworks to express pure psychological terror. The technique is called Expressionism, where artists distort reality to show inner emotions - notice how the swirling sky and wavy lines make the whole world seem unstable. The figure's skull-like face and open mouth aren't trying to look realistic but instead show how anxiety and fear feel from the inside. Munch used a combination of oil paint, tempera, and pastel to create different textures that add to the unsettling effect. This painting became a symbol of modern existential anxiety, influencing how we understand the relationship between art and mental health."
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
            description = "Monet painted over 250 water lily paintings in his garden at Giverny, studying how light and color changed throughout different times of day and seasons. This demonstrates the core of Impressionism - capturing fleeting moments and the effects of natural light rather than creating detailed, 'finished' paintings. Notice how Monet used broken brushstrokes and pure colors placed side by side, letting your eye 'mix' them instead of blending them on his palette. The large scale was revolutionary - these paintings surround viewers like an environment, predicting modern installation art. Monet continued this series even as his eyesight deteriorated, showing how personal vision and artistic dedication can transform physical limitations into creative breakthroughs."
        )
    )
}