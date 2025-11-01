package org.lanzadera.proyectos

// payload mínimo que cumple con isValidMovie()
fun movieListJsonCompleta(): String = """
{
  "page": 1,
  "results": [
    {
      "adult": false,
      "backdrop_path": "/b1.jpg",
      "genre_ids": [28,12],
      "id": 101,
      "original_language": "en",
      "original_title": "Foo",
      "overview": "Overview Foo",
      "popularity": 123.4,
      "poster_path": "/p1.jpg",
      "release_date": "2024-05-01",
      "title": "Foo",
      "video": false,
      "vote_average": 7.1,
      "vote_count": 321
    },
    {
      "adult": false,
      "backdrop_path": "/b2.jpg",
      "genre_ids": [35],
      "id": 202,
      "original_language": "en",
      "original_title": "Bar",
      "overview": "Overview Bar",
      "popularity": 98.7,
      "poster_path": "/p2.jpg",
      "release_date": "2024-06-10",
      "title": "Bar",
      "video": false,
      "vote_average": 6.8,
      "vote_count": 220
    }
  ],
  "total_pages": 1,
  "total_results": 2
}
""".trimIndent()

